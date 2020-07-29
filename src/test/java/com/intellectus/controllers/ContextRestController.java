package com.intellectus.controllers;

import com.intellectus.controllers.model.UserEditRequest;
import com.intellectus.exceptions.ExistUserException;
import com.intellectus.model.constants.Role;
import com.intellectus.payload.LoginRequest;
import com.intellectus.repositories.UserRepository;
import com.intellectus.services.impl.UserService;
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
        return LoginRequest.builder().username("admin@intellectus.com").password("admin").build();
    }

    protected LoginRequest viewerLogin() {
        return LoginRequest.builder().username("supervisor@intellectus.com").password("supervisor").build();
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
        userResponse.setUsername("admin@intellectus.com");
        userResponse.setEmail("admin@intellectus.com");
        userResponse.setPassword("admin");
        userResponse.setNewPassword("admin");
        userResponse.setConfirmNewPassword("admin");
        userResponse.setRole(Role.ROLE_ADMIN.role());
        userResponse.setName("Admin");
        userResponse.setLastName("Admin");
        userService.createOrEdit(userResponse);

        userResponse = new UserEditRequest();
        userResponse.setUsername("supervisor@intellectus.com");
        userResponse.setEmail("supervisor@intellectus.com");
        userResponse.setPassword("supervisor");
        userResponse.setNewPassword("supervisor");
        userResponse.setConfirmNewPassword("supervisor");
        userResponse.setRole(Role.ROLE_SUPERVISOR.role());
        userResponse.setName("Supervisor");
        userResponse.setLastName("Supervisor");
        userService.createOrEdit(userResponse);
    }

    @After
    public void after() {
        repository.deleteAll();
    }
}
