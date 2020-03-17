package com.atixlabs.services.impl;

import com.atixlabs.controllers.model.UserEditRequest;
import com.atixlabs.model.configuration.Menu;
import com.atixlabs.model.configuration.Permission;
import com.atixlabs.model.configuration.Role;
import com.atixlabs.repositories.*;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByCode(com.atixlabs.model.constants.Role.ROLE_ADMIN.role()) == null) {
            menuRepository.deleteAll();
            Menu mMainUser = menuRepository.save(Menu.builder().name("Profile")
                    .code("USERS")
                    .icon("user")
                    .order(3)
                    .build());
            Menu mViewUser = menuRepository.save(Menu.builder().name("View Profile")
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
            /*Menu mModifyUser = menuRepository.save(Menu.builder().name("Modificar Usuario")
                    .code("MODIFY_USER")
                    .icon("user")
                    .parent(mMainUser)
                    .uri("/user-information")
                    .type("POST")
                    .build());
            Menu mListUsers = menuRepository.save(Menu.builder().name("Usuarios")
                    .code("LIST_USER")
                    .icon("user")
                    .parent(mMainUser)
                    .uri("/users")
                    .type("GET")
                    .build());*/
            Menu mReports = menuRepository.save(Menu.builder().name("Reports")
                    .code("REPORTS")
                    .icon("area-chart")
                    .order(0)
                    .build());
            Menu mKnowledge = menuRepository.save(Menu.builder().name("Knowledge")
                    .code("KNOWLEDGE")
                    .icon("bulb")
                    .order(1)
                    .build());

            Menu mManageUsers = menuRepository.save(Menu.builder().name("Manage Users")
                    .code("MANAGE_USERS")
                    .icon("setting")
                    .order(2)
                    .uri("/administration")
                    .type("GET")
                    .build());

            Menu mUploadReport = menuRepository.save(Menu.builder().name("Upload Report")
                    .code("UPLOAD_REPORT")
                    .icon("setting")
                    .order(0)
                    .uri("/upload-report")
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
                    .code(com.atixlabs.model.constants.Role.ROLE_ADMIN.role())
                    .description("Administrator")
                    .menus(Sets.newHashSet(mMainUser, mKnowledge,mUploadReport, mManageUsers, mReports, mViewUser, mLogout))
                    .permissions(Sets.newHashSet(pCreateUser, pModifyUser, pViewListUser, pViewProfile))
                    .build());
            Role roleViewer = roleRepository.save(Role.builder()
                    .code(com.atixlabs.model.constants.Role.ROLE_VIEWER.role())
                    .description("Viewer")
                    .menus(Sets.newHashSet(mMainUser, mKnowledge, mReports, mViewUser, mLogout))
                    .permissions(Sets.newHashSet(pModifyUser, pViewProfile))
                    .build());

            Role roleAnalyst = roleRepository.save(Role.builder()
                    .code(com.atixlabs.model.constants.Role.ROLE_ANALYST.role())
                    .description("Analyst")
                    .menus(Sets.newHashSet(mMainUser, mKnowledge, mUploadReport, mReports, mViewUser, mLogout))
                    .permissions(Sets.newHashSet(pModifyUser, pViewProfile))
                    .build());
        }

        if (!userService.findByUsername("admin@atixlabs.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("admin@atixlabs.com");
            userResponse.setEmail("admin@atixlabs.com");
            userResponse.setPassword("admin");
            userResponse.setNewPassword("admin");
            userResponse.setConfirmNewPassword("admin");
            userResponse.setRole(com.atixlabs.model.constants.Role.ROLE_ADMIN.role());
            userResponse.setName("Admin");
            userResponse.setLastName("Admin");
            userService.createOrEdit(userResponse);
        }
        if (!userService.findByUsername("viewer@atixlabs.com").isPresent()) {
            UserEditRequest userResponse = new UserEditRequest();
            userResponse.setUsername("viewer@atixlabs.com");
            userResponse.setEmail("viewer@atixlabs.com");
            userResponse.setPassword("viewer");
            userResponse.setNewPassword("viewer");
            userResponse.setConfirmNewPassword("viewer");
            userResponse.setRole(com.atixlabs.model.constants.Role.ROLE_VIEWER.role());
            userResponse.setName("Viewer");
            userResponse.setLastName("Viewer");
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
