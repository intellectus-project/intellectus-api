package com.intellectus.services.impl;

import com.intellectus.controllers.model.UserEditRequest;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.Menu;
import com.intellectus.model.configuration.Permission;
import com.intellectus.model.configuration.Role;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.Emotion;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.*;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.findByCode(com.intellectus.model.constants.Role.ROLE_ADMIN.role()) == null) {
            menuRepository.deleteAll();
            Menu mViewDashboard = menuRepository.save(Menu.builder().name("Dashboard")
                    .code("VIEW_DASHBOARD")
                    .icon("bar-chart")
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
                    .menus(Sets.newHashSet(mViewDashboard, mMainUser, mManageUsers, mViewUser, mLogout))
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
                    .menus(Sets.newHashSet(mMainUser, mViewUser, mLogout))
                    .permissions(Sets.newHashSet(pModifyUser, pViewProfile))
                    .build());
        }

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
            userService.createOrEdit(userResponse);
        }

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
            userService.createOrEdit(userResponse);
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
}
