package com.intellectus.services.impl;

import com.intellectus.controllers.model.CallRequestPatchDto;
import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.StatDto;
import com.intellectus.controllers.model.UserEditRequest;
import com.intellectus.model.Call;
import com.intellectus.model.Shift;
import com.intellectus.model.Stat;
import com.intellectus.model.Weather;
import com.intellectus.model.configuration.Menu;
import com.intellectus.model.configuration.Permission;
import com.intellectus.model.configuration.Role;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.*;
import com.google.common.collect.Sets;
import com.intellectus.services.BreakService;
import com.intellectus.services.CallService;
import com.intellectus.services.StatService;
import com.intellectus.services.WeatherImageService;
import com.intellectus.services.newsEvent.NewsEventService;
import com.intellectus.services.weather.WeatherService;
import com.intellectus.utils.DbInitializerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class DBInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private CallRepository callRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private NewsEventRepository newsEventRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private NewsEventService newsEventService;

    @Autowired
    private CallService callService;

    @Autowired
    private StatService statService;

    @Autowired
    private BreakService breakService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherImageService weatherImageService;

    private String SHIFT_MAÑANA = "Mañana";
    private String SHIFT_TARDE = "Tarde";
    private String SHIFT_NOCHE = "Noche";


    @Override
    public void run(String... args) throws Exception {

        menusAndPermissions();
        newsEvents();
        weathers();
        weatherImages();
        shifts();
        users();
        calls();

    }

    private void menusAndPermissions(){
        if (roleRepository.findByCode(com.intellectus.model.constants.Role.ROLE_ADMIN.role()) == null) {
            menuRepository.deleteAll();
            Menu mOperators = menuRepository.save(Menu.builder().name("Operadores")
                    .code("OPERADORES")
                    .icon("team")
                    .uri("/operators")
                    .order(2)
                    .build());
            Menu mViewDashboard = menuRepository.save(Menu.builder().name("Dashboard")
                    .code("VIEW_DASHBOARD")
                    .icon("dashboard")
                    .uri("/dashboard")
                    .order(0)
                    .build());
            Menu mMainUser = menuRepository.save(Menu.builder().name("Perfil")
                    .code("USERS")
                    .icon("user")
                    .order(2)
                    .build());
            Menu mViewUser = menuRepository.save(Menu.builder().name("Ver Perfil")
                    .code("VIEW_PROFILE")
                    //.icon("user")
                    .parent(mMainUser)
                    .uri("/user-information")
                    .order(0)
                    .type("GET")
                    .build());
            Menu mLogout = menuRepository.save(Menu.builder().name("Logout")
                    .code("LOGOUT")
                    .icon("logout")
                    .parent(mMainUser)
                    .order(1)
                    .uri("/login")
                    .type("GET")
                    .build());

            Menu mManageUsers = menuRepository.save(Menu.builder().name("Usuarios")
                    .code("MANAGE_USERS")
                    .icon("setting")
                    .order(1)
                    .uri("/administration")
                    .type("GET")
                    .build());


            Permission pViewProfile = permissionRepository.save(Permission.builder()
                    .code("VIEW_PROFILE")
                    .description("Permiso ver perfil")
                    .build());

            Permission pCreateUser = permissionRepository.save(Permission.builder()
                    .code("CREATE_USER")
                    .description("Permiso crear usuario")
                    .build());

            Permission pViewListUser = permissionRepository.save(Permission.builder()
                    .code("LIST_USERS")
                    .description("Permiso Listar usuarios")
                    .build());

            Permission pModifyUser = permissionRepository.save(Permission.builder()
                    .code("MODIFY_USERS")
                    .description("Permiso Modificar usuarios")
                    .build());

            Role roleAdmin = roleRepository.save(Role.builder()
                    .code(com.intellectus.model.constants.Role.ROLE_ADMIN.role())
                    .description("Administrator")
                    .menus(Sets.newHashSet(mMainUser, mManageUsers, mViewUser, mLogout))
                    .permissions(Sets.newHashSet(pCreateUser, pModifyUser, pViewListUser, pViewProfile))
                    .build());
            Role roleOperator = roleRepository.save(Role.builder()
                    .code(com.intellectus.model.constants.Role.ROLE_OPERATOR.role())
                    .description("Viewer")
                    .menus(Sets.newHashSet(mMainUser, mViewUser, mLogout))
                    .permissions(Sets.newHashSet(pModifyUser, pViewProfile))
                    .build());

            Role roleSupervisor = roleRepository.save(Role.builder()
                    .code(com.intellectus.model.constants.Role.ROLE_SUPERVISOR.role())
                    .description("Analyst")
                    .menus(Sets.newHashSet(mViewDashboard, mOperators, mMainUser, mViewUser, mLogout))
                    .permissions(Sets.newHashSet(pModifyUser, pViewProfile))
                    .build());
        }

        Optional<Menu> menu = menuRepository.findByCode("USERS");
        if (menu.isPresent() && menu.get().getName().contains("Profile")) {
            menu.get().setName("My account");
            menuRepository.save(menu.get());
        }
        menu = menuRepository.findByCode("VIEW_PROFILE");
        if (menu.isPresent()) {
            menu.get().setIcon("user");
            menuRepository.save(menu.get());
        }
    }

    private void newsEvents() {
        LocalDateTime dateFrom = LocalDate.now().atStartOfDay();
        LocalDateTime dateTo = LocalDate.now().atTime(LocalTime.MAX);
        if(newsEventRepository.findAllByCreatedBetween(dateFrom, dateTo).size() == 0) {
            newsEventService.fetch();
        }
    }

    private void weathers() {
        LocalDateTime now = LocalDateTime.now();
        double temp = DbInitializerUtils.getRandomInt(0, 30);
        for(int i = 0; i <= 50; i++) {
            LocalDateTime time = now.minusHours(i).truncatedTo(ChronoUnit.HOURS);;
            if(weatherRepository.findByHour(time).size() == 0) {
                String desc = weatherService.DESCRIPTION_EXAMPLES[DbInitializerUtils.getRandomInt(0, weatherService.DESCRIPTION_EXAMPLES.length -1 )];
                double tempModifier = (double) DbInitializerUtils.getRandomInt(-10, 10) / 30.0;
                Weather weather = new Weather(desc, temp + tempModifier , time);
                weatherRepository.save(weather);
            }
        }
    }

    private void weatherImages() {
        weatherImageService.create("Niebla", "niebla.png");
        weatherImageService.create("Llovizna ligera", "lluvia.png");
        weatherImageService.create("Nubes", "nube.png");
        weatherImageService.create("Nubes rotas", "nube.png");
        weatherImageService.create("Lluvia ligera", "lluvia.png");
        weatherImageService.create("Cielo claro", "soleado.png", 7, 18);
        weatherImageService.create("Cielo claro", "moon.png", 19, 6);
        weatherImageService.create("Algo de nubes", "parcialmentenublado.png", 7, 18);
        weatherImageService.create("Algo de nubes", "cloudmoon.png", 19, 6);
        weatherImageService.create("Bruma" , "niebla.png");
        weatherImageService.create("Nubes dispersas", "parcialmentenublado.png", 7, 18);
        weatherImageService.create("Nubes dispersas", "cloudmoon.png", 19, 6);
        weatherImageService.create("Muy nuboso" , "nube.png");
    }

    private void shifts() {
        if(!shiftRepository.findShiftByName(SHIFT_MAÑANA).isPresent()) {
            Shift mañana = new Shift(SHIFT_MAÑANA, 0, 8);
            shiftRepository.save(mañana);
        }

        if(!shiftRepository.findShiftByName(SHIFT_TARDE).isPresent()) {
            Shift tarde = new Shift(SHIFT_TARDE, 8, 16);
            shiftRepository.save(tarde);
        }

        if(!shiftRepository.findShiftByName(SHIFT_NOCHE).isPresent()) {
            Shift noche = new Shift(SHIFT_NOCHE, 16, 24);
            shiftRepository.save(noche);
        }
    }

    private void users() throws Exception {
        // ADMIN
        if (!userService.findByUsername("admin@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("admin@intellectus.com");
            userResponse.setEmail("admin@intellectus.com");
            userResponse.setPassword("admin");
            userResponse.setNewPassword("admin");
            userResponse.setConfirmNewPassword("admin");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_ADMIN.role());
            userResponse.setName("Admin");
            userResponse.setLastName("Admin");
            userService.createOrEdit(userResponse);
        }

        // SUPERVISORS
        if (!userService.findByUsername("supervisor@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("supervisor@intellectus.com");
            userResponse.setEmail("supervisor@intellectus.com");
            userResponse.setPassword("supervisor");
            userResponse.setNewPassword("supervisor");
            userResponse.setConfirmNewPassword("supervisor");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_SUPERVISOR.role());
            userResponse.setName("Supervisor");
            userResponse.setLastName("Supervisor");
            userResponse.setShiftId(shiftRepository.findShiftByName(SHIFT_MAÑANA).get().getId());
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername("supervisorTarde@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("supervisorTarde@intellectus.com");
            userResponse.setEmail("supervisorTarde@intellectus.com");
            userResponse.setPassword("supervisorTarde");
            userResponse.setNewPassword("supervisorTarde");
            userResponse.setConfirmNewPassword("supervisorTarde");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_SUPERVISOR.role());
            userResponse.setName("Fausto");
            userResponse.setLastName("Vera");
            userResponse.setShiftId(shiftRepository.findShiftByName(SHIFT_TARDE).get().getId());
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername("supervisorNoche@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("supervisorNoche@intellectus.com");
            userResponse.setEmail("supervisorNoche@intellectus.com");
            userResponse.setPassword("supervisorNoche");
            userResponse.setNewPassword("supervisorNoche");
            userResponse.setConfirmNewPassword("supervisorNoche");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_SUPERVISOR.role());
            userResponse.setName("Nicolás");
            userResponse.setLastName("Capaldo");
            userResponse.setShiftId(shiftRepository.findShiftByName(SHIFT_NOCHE).get().getId());
            userService.createOrEdit(userResponse);
        }

        // OPERATORS
        if (!userService.findByUsername("operator@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("operator@intellectus.com");
            userResponse.setEmail("operator@intellectus.com");
            userResponse.setPassword("operator");
            userResponse.setNewPassword("operator");
            userResponse.setConfirmNewPassword("operator");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_OPERATOR.role());
            userResponse.setName("Operator");
            userResponse.setLastName("Operator");
            userResponse.setSupervisor(userService.findByUsername("supervisor@intellectus.com").get());
            userResponse.setShiftId(shiftRepository.findShiftByName(SHIFT_MAÑANA).get().getId());
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername("lucas@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("lucas@intellectus.com");
            userResponse.setEmail("lucas@intellectus.com");
            userResponse.setPassword("lucas");
            userResponse.setNewPassword("lucas");
            userResponse.setConfirmNewPassword("lucas");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_OPERATOR.role());
            userResponse.setName("Lucas");
            userResponse.setLastName("Charlab");
            userResponse.setSupervisorId(userService.findByUsername("supervisor@intellectus.com").get().getId());
            userResponse.setShiftId(shiftRepository.findShiftByName(SHIFT_MAÑANA).get().getId());
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername("ronan@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("ronan@intellectus.com");
            userResponse.setEmail("ronan@intellectus.com");
            userResponse.setPassword("ronan");
            userResponse.setNewPassword("ronan");
            userResponse.setConfirmNewPassword("ronan");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_OPERATOR.role());
            userResponse.setName("Ronan");
            userResponse.setLastName("Vinitzca");
            userResponse.setSupervisorId(userService.findByUsername("supervisorTarde@intellectus.com").get().getId());
            userResponse.setShiftId(shiftRepository.findShiftByName(SHIFT_TARDE).get().getId());
            userService.createOrEdit(userResponse);
        }

        if (!userService.findByUsername("eric@intellectus.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("eric@intellectus.com");
            userResponse.setEmail("eric@intellectus.com");
            userResponse.setPassword("eric");
            userResponse.setNewPassword("eric");
            userResponse.setConfirmNewPassword("eric");
            userResponse.setRole(com.intellectus.model.constants.Role.ROLE_OPERATOR.role());
            userResponse.setName("Eric");
            userResponse.setLastName("Stoppel");
            userResponse.setSupervisorId(userService.findByUsername("supervisorNoche@intellectus.com").get().getId());
            userResponse.setShiftId(shiftRepository.findShiftByName(SHIFT_NOCHE).get().getId());
            userService.createOrEdit(userResponse);
        }
    }

    private void calls() {
        LocalDateTime date = LocalDateTime.now().minusDays(2);
        List<User> users = new ArrayList<>();
        users.add(userService.findByUsername("lucas@intellectus.com").get());
        users.add(userService.findByUsername("ronan@intellectus.com").get());
        users.add(userService.findByUsername("eric@intellectus.com").get());

        createCalls(users, date);
        createCalls(users, date.plusDays(1));
        createCalls(users, date.plusDays(2));
    }

    private void createCalls(List<User> users, LocalDateTime date){
        if (callService.fetchByDay(date.toLocalDate()).size() > 3) return;
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            users.forEach(user -> {
                createCall(user, date, finalI);
            });
        }
    }

    private void createCall(User user, LocalDateTime date, int i) {
        try {
            LocalDateTime startDate = date.plusHours(i);
            LocalDateTime endDate = startDate.plusMinutes(DbInitializerUtils.getRandomInt(5, 85));
            Long callId = callService.create(user, CallRequestPostDto.builder().startTime(startDate).build());
            List<Double> consultantStats = DbInitializerUtils.randomStats();
            StatDto consultantDto = StatDto.builder()
                    .anger(consultantStats.get(0))
                    .fear(consultantStats.get(1))
                    .happiness(consultantStats.get(2))
                    .neutrality(consultantStats.get(3))
                    .sadness(consultantStats.get(4))
                    .build();

            List<Double> operatorStats = DbInitializerUtils.randomStats();
            StatDto operatorDto = StatDto.builder()
                    .anger(operatorStats.get(0))
                    .fear(operatorStats.get(1))
                    .happiness(operatorStats.get(2))
                    .neutrality(operatorStats.get(3))
                    .sadness(operatorStats.get(4))
                    .build();

            CallRequestPatchDto callDto = CallRequestPatchDto.builder()
                    .consultantStats(consultantDto)
                    .operatorStats(operatorDto)
                    .endTime(endDate)
                    .emotion(DbInitializerUtils.getRandomInt(0, 4))
                    .build();

            callService.update(callDto, callId);
            if (DbInitializerUtils.getRandomInt(1,100) % 10 == 0) {
                //TODO hacer random la duración del descanso
                breakService.create(callService.findById(callId).get(), 10, false, true);
            }
        } catch (Exception e) {
            System.out.println("Error creando call de prueba");
            System.out.println(e.getStackTrace());
        }
    }
}
