package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.UserRequestModel;
import models.UserResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateUserTests {
    private final String expectedName = "Sting";
    private final String expectedJob = "singer";

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api/users";
    }

    @Test
    @DisplayName("Создать пользователя со всеми полями")
    void createUserTest() {

        UserRequestModel model = new UserRequestModel(expectedName, expectedJob);

        UserResponseModel responseModel =
                step("Make request", () ->
                     given()
                            .filter(withCustomTemplates())
                            .log().uri()
                            .log().body()
                            .log().headers()
                            .body(model)
                            .contentType(ContentType.JSON)

                            .when()
                            .post("")

                            .then()
                            .log().status()
                            .log().body()
                            .statusCode(201)
                            .extract().as(UserResponseModel.class));

        step("Check response", () -> {
            assertEquals(expectedName, responseModel.getName());
            assertEquals(expectedJob, responseModel.getJob());
        });
    }

    @Test
    @DisplayName("Создать пользователя без указания имени")
    void createUserTestWithoutName() {
        UserRequestModel model = UserRequestModel.builder()
                .job(expectedJob)
                .build();

        UserResponseModel responseModel = given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .body(model)
                .contentType(ContentType.JSON)

                .when()
                .post("")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(UserResponseModel.class);

        assertNull(responseModel.getName());
        assertEquals(expectedJob, responseModel.getJob());
    }
}


