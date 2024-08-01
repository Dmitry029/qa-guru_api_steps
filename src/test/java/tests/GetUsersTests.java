package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserDataModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetUsersTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Получить размер списка всех пользователей")
    void getSizeOfAllUsersList() {
        int allUsers = 12;
        given()
                .log().uri()
                .when()
                .get("/users?page2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(allUsers));
    }

    @Test
    @DisplayName("Получить размер списка пользователей на странице")
    void getUsersListSizeOnThePage() {
        int usersListSize = 6;
        given()
                .log().uri()
                .when()
                .get("/users?page2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data", hasSize(usersListSize));
    }

    @Test
    @DisplayName("Сравнить ожидаемый список Id с полученным после запроса")
    void compareExpectedListOfIdsAndActual() {
        List<Integer> expectedId = List.of(1, 2, 3, 4, 5, 6);
        Response response = given()
                .log().uri()
                .when()
                .get("/users?page2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();
        List<Integer> actualId = response.jsonPath().getList("data.id");
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Проверить, что у всех пользователей почта оканчивается на @regres.in")
    void compare() {
        String expectedEndOfEmail = "@reqres.in";
        List<UserDataModel> users = given()
                .log().uri()
                .when()
                .get("/users?page2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", UserDataModel.class);

        assertTrue(users.stream().allMatch(e -> e.getEmail().endsWith(expectedEndOfEmail)));
    }
}