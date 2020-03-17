package com.atixlabs;

import com.atixlabs.controllers.AuthController;
import com.atixlabs.controllers.UsersController;
import com.atixlabs.model.configuration.User;
import com.atixlabs.payload.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class IntegrationTests {

    @LocalServerPort
    private int port;

    //@Autowired
    //ObjectMapper objectMapper;

    private String token;

    private String login(LoginRequest logging) {
        RestAssured.port = this.port;
        token = given()
                .contentType(ContentType.JSON)
                .body(logging)
                .when().post(AuthController.URL_MAPPING)
                .andReturn().jsonPath().getString("accessToken");
        log.debug("Got token:" + token);
        return token;
    }
    /*
        Test if user admin have permissions about find all users and not exist users havent permissions
     */
    @Test
    public void findAll() {
        String token = login(LoginRequest.builder().username("admin@atixlabs.com").password("admin").build());
        Response response = given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .when().get(UsersController.URL_MAPPING_USERS);

        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        List<User> users = response.as(new TypeRef<List<User>>() {});
        Assert.assertTrue(users.size() > 0);

        token = login(LoginRequest.builder().username("admin1").password("admin1").build());
        response = given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .when().get(UsersController.URL_MAPPING_USERS);

        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
