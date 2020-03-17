package com.atixlabs.controllers;

import com.atixlabs.controllers.model.UserEditRequest;
import com.atixlabs.exceptions.ExistUserException;
import com.atixlabs.model.constants.Role;
import com.atixlabs.payload.LoginRequest;
import com.atixlabs.repositories.UserRepository;
import com.atixlabs.services.impl.UserService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public abstract class ContextRestController {

    @LocalServerPort
    protected int port;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository repository;

    protected LoginRequest adminLogin() {
        return LoginRequest.builder().username("admin@atixlabs.com").password("admin").build();
    }

    protected LoginRequest viewerLogin() {
        return LoginRequest.builder().username("viewer@atixlabs.com").password("viewer").build();
    }

    protected String login(LoginRequest logging) {
        String token = given()
                .contentType(ContentType.JSON)
                .body(logging)
                .when().post(AuthController.URL_MAPPING)
                .andReturn().jsonPath().getString("accessToken");
        log.debug("Got token:" + token);
        return token;
    }

    protected boolean logout(String token) {
        Response response = given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .when().get("/api/auth/logout");
        return response
                .getStatusCode() == HttpStatus.SC_OK;
    }

    @Before
    public void beforeLoggin() throws ExistUserException {
        repository.deleteAll();
        UserEditRequest userResponse = new UserEditRequest();
        userResponse.setUsername("admin@atixlabs.com");
        userResponse.setEmail("admin@atixlabs.com");
        userResponse.setPassword("admin");
        userResponse.setNewPassword("admin");
        userResponse.setConfirmNewPassword("admin");
        userResponse.setRole(Role.ROLE_ADMIN.role());
        userResponse.setName("Admin");
        userResponse.setLastName("Admin");
        userService.createOrEdit(userResponse);

        userResponse = new UserEditRequest();
        userResponse.setUsername("viewer@atixlabs.com");
        userResponse.setEmail("viewer@atixlabs.com");
        userResponse.setPassword("viewer");
        userResponse.setNewPassword("viewer");
        userResponse.setConfirmNewPassword("viewer");
        userResponse.setRole(Role.ROLE_VIEWER.role());
        userResponse.setName("Viewer");
        userResponse.setLastName("Viewer");
        userService.createOrEdit(userResponse);
    }

    @After
    public void after() {
        repository.deleteAll();
    }
}
