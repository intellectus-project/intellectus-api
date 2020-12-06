package com.intellectus.services.impl;

import com.intellectus.controllers.model.*;
import com.intellectus.exceptions.*;
import com.intellectus.model.*;
import com.intellectus.model.configuration.Menu;
import com.intellectus.model.configuration.Role;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.repositories.RoleRepository;
import com.intellectus.repositories.UserRepository;
import com.intellectus.repositories.UserWebPushCredentialsRepository;
import com.intellectus.services.BreakService;
import com.intellectus.services.CallService;
import com.intellectus.services.ShiftService;
import com.intellectus.services.StatService;
import com.intellectus.services.filters.FilterUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.*;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String role_code_frontend = "role";
    private static final String type_code_frontend = "type";
    private static final String countryOrZone_code_frontend = "selectedKey";

    @Autowired
    UserRepository repository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CallService callService;

    @Autowired
    StatService statService;

    @Autowired
    ShiftService shiftService;

    @Autowired
    BreakService breakService;

    @Autowired
    UserWebPushCredentialsRepository userWebPushCredentialsRepository;

    @Override
    public Collection<User> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
    }

    @Override
    public User disable(Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setActive(false);
                    return repository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not exist."));
    }

    @Override
    public User enable(Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setActive(true);
                    return repository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not exist."));
    }

    // TODO: Ver excepciones
    @Override
    public User createOrEdit(UserEditRequest userResponse) throws ExistUserException {
        User user;
        if (userResponse.getId() != null) {
            user = repository.findById(userResponse.getId())
                    .orElseThrow(() -> new RuntimeException());
            if (Strings.isNullOrEmpty(userResponse.getPassword())
                    || !passwordEncoder.matches(userResponse.getPassword(), user.getPassword())) {
                throw new RuntimeException();
            }
        }
        user = repository.findByUsername(userResponse.getUsername());
        if (user != null) {
            throw new ExistUserException(user);
        } else {
            user = new User();
        }

        if (Strings.isNullOrEmpty(userResponse.getNewPassword())
            || Strings.isNullOrEmpty(userResponse.getConfirmNewPassword())
            || !userResponse.getNewPassword().equals(userResponse.getConfirmNewPassword())) {
            throw new RuntimeException("Password not found.");
        }

        Long shiftId = userResponse.getShiftId();
        Optional<Shift> shift = Optional.empty();
        if (shiftId != null)
             shift = shiftService.findById(shiftId);

        Long supervisorId = userResponse.getSupervisorId();
        User supervisor = null;
        if (supervisorId != null)
            supervisor = this.findById(supervisorId);

        Role role = roleRepository.findByCode(userResponse.getRole());
        user.setName(userResponse.getName());
        user.setLastName(userResponse.getLastName());
        user.setEmail(userResponse.getEmail());
        user.setPhone(userResponse.getPhone());
        user.setPassword(userResponse.getNewPassword());
        user.setRole(role);
        user.setUsername(userResponse.getUsername());
        user.setShift(shift.isPresent() ? shift.get() : user.getShift());
        user.setSupervisor(supervisor != null ? supervisor : user.getSupervisor());
        return repository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(repository.findByUsername(username));
    }

    @Override
    public Optional<AuthenticatedUserDto> findUserAuthenticated(String username, String password) throws InactiveUserException {
        Optional<User> user = this.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            if (!user.get().isActive()) {
                throw new InactiveUserException(user.get());
            }
            AuthenticatedUserDto authenticatedUser = new AuthenticatedUserDto();
            authenticatedUser.setId(user.get().getId());
            authenticatedUser.setUsername(username);
            authenticatedUser.setEmail(user.get().getEmail());
            authenticatedUser.setName(user.get().getName());
            authenticatedUser.setLastname(user.get().getLastName());
            authenticatedUser.setRole(user.get().getRole().getDescription());
            authenticatedUser.setPermissions(Sets.newHashSet(this.findPermissionsByRole(user.get().getRole())));
            authenticatedUser.setNavbar(NavbarUser.builder().logo("logo").menus(Sets.newLinkedHashSet()).build());

            Collection<Menu> navbar = user.get().getRole().getMenus();
            for (Menu menu: navbar) {
                if (menu.isMainMenu()) {
                    MenuDto menuDto = new MenuDto(menu);
                    menuDto.getItems().addAll(navbar.stream()
                                                    .filter(m -> !m.isMainMenu() && m.getParent().equals(menu))
                                                    .sorted(Comparator.comparingInt(Menu::getOrder))
                                                    .map(n -> new MenuItemDto(n))
                                                    .collect(Collectors.toList()));
                    authenticatedUser.getNavbar().getMenus().add(menuDto);
                }
            }
            return Optional.of(authenticatedUser);
        }

        return Optional.empty();
    }

    @Override
    public Collection<String> findPermissions(Long id) {
        User user = findById(id);
        return findPermissionsByRole(user.getRole());
    }

    //TODO: MANAGE EXCEPTIONS CORRECTLY
    @Override
    public Optional<User> updateUser(Map<String, Object> updates, Long id) throws Exception{

        User existentUser;
        User updateUser;
        User third;
        Optional<String> password = Optional.empty();
        Role role = null;

        if(id == null || id < 1) return Optional.empty();
        existentUser = repository.findById(id).orElseThrow(() -> new InexistentUserException());

        if(updates.containsKey(role_code_frontend)){
            role = roleRepository.findByCode(updates.get(role_code_frontend).toString());
        }

        if(updates.containsKey("password") &&
            !passwordEncoder.matches(updates.get("password").toString(), existentUser.getPassword()))
        {
            log.warn(String.format("Error changed password. Password not match for user %s", existentUser.getUsername()));
            throw new PasswordNotMatchException();
        }
        if(updates.containsKey("newPassword") && updates.containsKey("confirmNewPassword")){
            if(updates.get("newPassword").equals(updates.get("confirmNewPassword"))){
                password = Optional.of(passwordEncoder.encode(updates.get("newPassword").toString()));
            }
        }
        User tempUser = repository.findByUsername(updates.get("username").toString());
        if(updates.containsKey("username")
                && tempUser != null
                && updates.get("username").equals(existentUser.getUsername())
                && tempUser.getId() != id)
        {
            log.warn("Username already exists");
            return Optional.empty();
        }
        Optional<Shift> shift = Optional.empty();
        if(updates.containsKey("shiftId")) {
            shift = shiftService.findById(Long.valueOf(updates.get("shiftId").toString()));
        }

        User supervisor = null;
        if(updates.containsKey("supervisorId")) {
            supervisor = this.findById(Long.valueOf(updates.get("supervisorId").toString()));
        }

        updates = parseUpdatesForUser(updates);
        updateUser = new ObjectMapper().convertValue(updates, User.class);
        if(role != null) updateUser.setRole(role);
        if(password.isPresent()) updateUser.setPassword(password.get());
        if (shift.isPresent()) updateUser.setShift(shift.get());
        if (supervisor != null) updateUser.setSupervisor(supervisor);

        try {
            third = com.intellectus.utils.ObjectMapper.mergeObjects(updateUser,existentUser);
            repository.save(third);
            return Optional.of(third);
        } catch (Exception e){
            log.error(e.getStackTrace().toString());
            return Optional.empty();
        }
    }

    private Map<String, Object> parseUpdatesForUser(Map<String, Object> updates) {
        updates.remove(type_code_frontend);
        updates.remove(countryOrZone_code_frontend);
        updates.remove(role_code_frontend);
        updates.remove("newPassword");
        updates.remove("confirmNewPassword");
        updates.remove("shiftId");
        updates.remove("supervisorId");
        return updates;
    }

    @Override
    public Collection<User> findAll(FilterUserDto filter) {
        return repository.findAll(getUserSpecification(filter));
    }

    @Override
    public Page<User> findUsersFilteredAndPaginated(FilterUserDto filter, Pageable page) {
        return repository.findAll(getUserSpecification(filter), page);
    }

    private Specification<User> getUserSpecification(FilterUserDto filter){
        return (Specification<User>) (root, query, cb) -> {
            List<Predicate> predicates = Lists.newLinkedList();
            if (!Strings.isNullOrEmpty(filter.getSearch())) {
                String search = "%".concat(filter.getSearch()).concat("%").toUpperCase();
                predicates.add(cb.or(
                        cb.like(cb.upper(root.get("name")), search),
                        cb.like(cb.upper(root.get("lastName")), search),
                        cb.like(cb.upper(root.get("username")), search)
                ));
            }
            if (filter.getId().isPresent()) {
                predicates.add(cb.notEqual(root.get("id"), filter.getId().get()));
            }
            if (filter.getEnabled().isPresent()) {
                predicates.add(cb.equal(root.get("active"), filter.getEnabled().get()));
            }
            if (!Strings.isNullOrEmpty(filter.getRole().orElse(null))) {
                String search = "%".concat(filter.getRole().get()).concat("%").toUpperCase();
                predicates.add(cb.like(cb.upper(root.get("role").get("description")), search));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

    }

    private Collection<String> findPermissionsByRole(Role role) {
        return role.getPermissions().stream().map(p -> p.getCode()).collect(Collectors.toList());
    }

    @Override
    public Set<MenuDto> findNavbar(Long id) {
        User user = findById(id);
        return findNavbarDto(user.getRole());
    }

    private Set<MenuDto> findNavbarDto(Role role) {
        Set<MenuDto> navbar = Sets.newHashSet();
        for (Menu menu: role.getMenus()) {
            if (menu.isMainMenu()) {
                MenuDto menuDto = new MenuDto(menu);
                menuDto.getItems().addAll(menu.getItems().stream()
                        .map(n -> new MenuItemDto(n))
                        .collect(Collectors.toList()));
                navbar.add(menuDto);
            }
        }
        return navbar;
    }

    public Collection<User> getOperatorsBySupervisor(Long supervisorId) {
        return repository.findOperatorsBySupervisorId(supervisorId);
    }

    public Collection<OperatorDto> getOperatorsWithInfoBySupervisor(Long supervisorId) {
        Collection<User> users = getOperatorsBySupervisor(supervisorId);
        List<OperatorDto> operators = new ArrayList<>();
        users.forEach(user -> {
            boolean atBreak = atBreak(user);
            boolean breakAssigned = breakAssignedBySupervisor(user);
            Optional<Stat> stat = statService.lastOperatorStat(user);
            OperatorDto dto = user.toOperatorDto(callService.actualOperatorCall(user) != null ? callService.actualOperatorCall(user).getStartTime() : null,
                                                 stat.map(Stat::getPrimaryEmotion).orElse(null),
                                                 stat.map(Stat::getSecondaryEmotion).orElse(null),
                                                 atBreak,
                                                 breakAssigned);
            operators.add(dto);
        });
        return operators;
    }

    public boolean atBreak(User user) {
        Optional<Break> breakOpt = breakService.findLastByUser(user, true);
        if (breakOpt.isPresent()){
            Break breakObj = breakOpt.get();
            return breakService.isActive(breakObj);
        } else return false;
    }

    public long remainingBreakTime(User user){
        Optional<Break> breakOpt = breakService.findLastByUser(user, true);
        long rem = breakOpt.map(br -> breakService.remainingBreakTime(br)).orElse(0l);
        return rem;
    }

    public boolean breakAssignedBySupervisor(User user) {
        return breakService.findLastByUser(user, false).isPresent();
    }

    public void assignSupervisorToOperator(User supervisor, Long operatorId) {
        User operator = findById(operatorId);
        if (!operator.getRole().getCode().equals(com.intellectus.model.constants.Role.ROLE_OPERATOR.role()))
            throw new RuntimeException("The id provided does not correspond to an operator");
        operator.setSupervisor(supervisor);
        repository.save(operator);
    }

    public boolean exists(Long userId){
        try {
            this.findById(userId);
            return true;
        }catch (UsernameNotFoundException unfe){
            return false;
        }
    }

    public List<User> getSupervisors() {
        Role role = roleRepository.findByCode(com.intellectus.model.constants.Role.ROLE_SUPERVISOR.role());
        return repository.findAllByRole(role);
    }

    public EmotionStatusDto getOperatorEmotionStatus(User operator) {
        Optional<Stat> opStat = statService.lastOperatorStat(operator);
        if(!opStat.isPresent()) {
            return EmotionStatusDto.builder().build();
        }
        Stat stat = opStat.get();
        StatDto statDto = StatDto.builder()
                      .sadness(stat.getSadness())
                      .anger(stat.getAnger())
                      .fear(stat.getFear())
                      .happiness(stat.getHappiness())
                      .neutrality(stat.getNeutrality())
                      .build();
        return EmotionStatusDto.builder()
                .status(statDto)
                .name(operator.getName())
                .atBreak(atBreak(operator))
                .breakAssignedToActualCall(breakAssignedBySupervisor(operator))
                .build();
    }

    public EmotionTablesDto getEmotionTables(User operator, LocalDate date){
        List<Stat> stats = statService.getByOperatorAndDate(operator, date);
        EmotionTablesDto dto = new EmotionTablesDto();
        stats.forEach(stat -> {
            dto.addAnger(stat.getAnger());
            dto.addFear(stat.getFear());
            dto.addHappiness(stat.getHappiness());
            dto.addSadness(stat.getSadness());
            dto.addNeutrality(stat.getNeutrality());
        });
        return dto;
    }

    public void registerWebPush(User user, RegisterUserWebPushDto dto) {
        UserWebPushCredentials credentials = new UserWebPushCredentials();
        credentials.setEndpoint(dto.getEndpoint());
        credentials.setAuth(dto.getAuth());
        credentials.setP256dh(dto.getP256dh());
        credentials.setUser(user);
        userWebPushCredentialsRepository.save(credentials);
    }
}
