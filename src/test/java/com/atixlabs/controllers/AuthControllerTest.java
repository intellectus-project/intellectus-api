package com.atixlabs.controllers;

import com.atixlabs.payload.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class AuthControllerTest extends ContextRestController{

    private Response findAllUsersRequest(String token) {
        return given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .when().get(UsersController.URL_MAPPING_USERS);
    }

    @Test
    public void loginSuccessful() {
        RestAssured.port = this.port;
        Response response = given()
                .contentType(ContentType.JSON)
                .body(LoginRequest.builder().username("admin@atixlabs.com").password("admin").build())
                .when().post(AuthController.URL_MAPPING);

        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void loginUnsuccessful() {
        RestAssured.port = this.port;
        given()
                .contentType(ContentType.JSON)
                .body(LoginRequest.builder().username("admin1").password("admin").build())
                .when().post(AuthController.URL_MAPPING)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    /*
        Test for 2 different users generate 2 different tokes
        Test for the same user burns old token and check not authorization with this token
     */
    @Test
    public void burnToken() {
        RestAssured.port = this.port;
        String token1 = login(adminLogin());
        String token2 = login(viewerLogin());
        Assert.assertNotEquals(token1, token2);
        findAllUsersRequest(token1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        Assert.assertTrue(logout(token1));
        findAllUsersRequest(token1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
        String token3 = login(adminLogin());
        findAllUsersRequest(token3)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
}
