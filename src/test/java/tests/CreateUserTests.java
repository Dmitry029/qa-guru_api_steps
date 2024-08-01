package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.UserRequestModel;
import models.UserResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        given()
                .body(model)
                .contentType(ContentType.JSON)
                .log().uri()
                .log().body()
                .log().headers()

                .when()
                .post("")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(UserResponseModel.class);

        assertEquals(expectedName, model.getName());
        assertEquals(expectedJob, model.getJob());
    }

    @Test
    @DisplayName("Создать пользователя без указания имени")
    void createUserTestWithoutName() {
        UserRequestModel model = UserRequestModel.builder()
                .job(expectedJob)
                .build();

        given()
                .body(model)
                .contentType(ContentType.JSON)
                .log().uri()
                .log().uri()
                .log().body()
                .log().headers()

                .when()
                .post("")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(UserResponseModel.class);

        assertNull(model.getName());
        assertEquals(expectedJob, model.getJob());
    }
}


