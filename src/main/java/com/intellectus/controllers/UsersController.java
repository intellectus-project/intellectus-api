package com.intellectus.controllers;

import com.google.common.collect.Collections2;
import com.intellectus.controllers.model.CallResponseDto;
import com.intellectus.controllers.model.MenuDto;
import com.intellectus.controllers.model.OperatorDto;
import com.intellectus.controllers.model.UserEditRequest;
import com.intellectus.exceptions.ExistUserException;
import com.intellectus.exceptions.PasswordNotMatchException;
import com.intellectus.exceptions.InexistentUserException;
import com.intellectus.model.configuration.User;
import com.intellectus.repositories.UserRepository;
import com.intellectus.security.UserPrincipal;
import com.intellectus.services.filters.FilterUserDto;
import com.intellectus.services.impl.UserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RestController
@Slf4j
@Validated
@RequestMapping(UsersController.URL_MAPPING_USERS)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class UsersController {

    public static final String URL_MAPPING_USERS = "/users";

    private final UserService service;
    private final UserRepository repository;

    public UsersController(UserService service, UserRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/filters")
    public Page<User> findAllWithFilters(@AuthenticationPrincipal UserPrincipal usuarioActual,
                                    @RequestParam("page") @Min(0) @Max(9999999) int page,
                                    @RequestParam("size") @Min(1) @Max(9999) int size,
                                    @RequestParam(required=false) String searchCriteria,
                                    @RequestParam(required=false)  Boolean enabled,
                                    @RequestParam(required=false) String role,
                                    @RequestParam(required=false) String zone,
                                    @RequestParam(required=false) String country) {
        Optional<Long> ignoreId = usuarioActual != null && usuarioActual.getId()!= null ? Optional.of(usuarioActual.getId()) : Optional.empty();
        Pageable pageRequest = PageRequest.of(page,size, Sort.by("name").ascending().and(Sort.by("lastName").ascending()));
        return service.findUsersFilteredAndPaginated(FilterUserDto.builder()
                .id(ignoreId)
                .search(searchCriteria)
                .enabled(Optional.ofNullable(enabled))
                .role(Optional.ofNullable(role))
                .zone(Optional.ofNullable(zone))
                .country(Optional.ofNullable(country))
                .build(),
                pageRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Collection<User> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(params = { "page", "size" })
    public Page<User> findPaginated(@AuthenticationPrincipal UserPrincipal usuarioActual,
                                    @RequestParam("page") @Min(0) @Max(9999999) int page,
                                    @RequestParam("size") @Min(1) @Max(9999) int size,
                                    @RequestParam(required=false) String searchCriteria) {
        Optional<Long> ignoreId = usuarioActual != null && usuarioActual.getId()!= null ? Optional.of(usuarioActual.getId()) : Optional.empty();
        Pageable pageRequest = PageRequest.of(page,size, Sort.by("name").ascending().and(Sort.by("lastName").ascending()));
        Page<User> allUsers = service.findUsersFilteredAndPaginated(FilterUserDto.builder().id(ignoreId).search(searchCriteria).build(),pageRequest);
        return allUsers;
    }

    @PreAuthorize("@permission.hasRole(#usuarioActual, 'ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@AuthenticationPrincipal UserPrincipal usuarioActual, @RequestBody @Valid UserEditRequest user) throws ExistUserException {
        return service.createOrEdit(user);
    }

    // TODO: modificar excepciones
    @PreAuthorize("@permission.hasRole(authentication.principal, 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public User findOne(@PathVariable @Min(1) Long id) {
        return repository.findById(id)
                        .orElseThrow(() -> new RuntimeException());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/disable/{id}")
    public void disable(@PathVariable @Min(1) Long id) {
        service.disable(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/enable/{id}")
    public void enable(@PathVariable @Min(1) Long id) {
        service.enable(id);
    }

    @GetMapping("/permissions/{id}")
    public Collection<String> findPermissions(@PathVariable @Min(1) Long id) {
        return service.findPermissions(id);
    }

    @GetMapping("/navbar")
    public Collection<MenuDto> getNavbar(@AuthenticationPrincipal UserPrincipal usuarioActual) {
        return service.findNavbar(usuarioActual.getId());
    }

    @PreAuthorize("@permission.canUpdate(#usuarioActual, #id,'ROLE_ADMIN' )")
    @PutMapping("/{id}")
    public ResponseEntity<User> putUser(@AuthenticationPrincipal UserPrincipal usuarioActual,
                                           @PathVariable @Min(1) Long id,
                                           @RequestBody @Valid UserEditRequest updatedUser) throws ExistUserException {

        try{
            service.findById(id);
        }catch (UsernameNotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (id != updatedUser.getId()){return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);}

        return  ResponseEntity.status(HttpStatus.OK).body(service.createOrEdit(updatedUser));
    }

    //TODO: check password to see if it is valid, maybe map to a @validpassword attribute in a new class.
    @PreAuthorize("@permission.canUpdate(#usuarioActual, #id,'ROLE_ADMIN' )")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal UserPrincipal usuarioActual,
                                           @PathVariable @Min(1) Long id,
                                           @RequestBody Map<String, Object> updates) throws Exception {
        Optional<User> updatedUser;
        try{
             updatedUser = service.updateUser(updates, id);
        }catch (InexistentUserException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (PasswordNotMatchException e) {
            return  ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Passwords do not match.");
        }

        if (!updatedUser.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return  ResponseEntity.status(HttpStatus.OK).body("Updated succesfully");
    }

    @GetMapping("/profile")
    public User getProfile(@AuthenticationPrincipal UserPrincipal usuarioActual) {
        return repository.findById(usuarioActual.getId())
                .orElseThrow(() -> new RuntimeException());
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/operators")
    public ResponseEntity<?> getOperatorsBySupervisor(@AuthenticationPrincipal UserPrincipal user)
    {
        Collection<OperatorDto> operators = service.getOperatorsWithInfoBySupervisor(user.getId());
        return ResponseEntity.ok().body(operators);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PostMapping("/assignSupervisor")
    public ResponseEntity<?> assignSupervisorToOperator(@AuthenticationPrincipal UserPrincipal supervisor,
                                                        @RequestParam @Min(1) Long id)
    {
        try{
            service.assignSupervisorToOperator(service.findById(supervisor.getId()), id);
        }catch (UsernameNotFoundException unfe){
            return ResponseEntity.badRequest().body(String.format("Supervisor with id %s not found", id));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body("assigned.");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/supervisors")
    public ResponseEntity<?> getSupervisors(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(service.getSupervisors());
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_OPERATOR')")
    @GetMapping("/operatorEmotionStatus")
    public ResponseEntity<?> getOperatorEmotionStatus(@AuthenticationPrincipal UserPrincipal operator,
                                                      @RequestParam Optional<Long> operatorId)
    {
        User user = operatorId.isPresent() ? service.findById(operatorId.get()) : service.findById(operator.getId());
        return ResponseEntity.ok().body(service.getOperatorEmotionStatus(user));
    }
}
