package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserDataModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static specs.GetUserSpec.getUserResponseSpec;
import static specs.GetUserSpec.getUserSpec;

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
        given(getUserSpec)
                .when()
                .get("/users?page2")
                .then()
                .spec(getUserResponseSpec)
                .body("total", is(allUsers));
    }

    @Test
    @DisplayName("Получить размер списка пользователей на странице")
    void getUsersListSizeOnThePage() {
        int usersListSize = 6;
        given(getUserSpec)
                .when()
                .get("/users?page2")
                .then()
                .spec(getUserResponseSpec)
                .body("data", hasSize(usersListSize));
    }

    @Test
    @DisplayName("Сравнить ожидаемый список Id с полученным после запроса")
    void compareExpectedListOfIdsAndActual() {
        List<Integer> expectedId = List.of(1, 2, 3, 4, 5, 6);
        Response response =
                step("Make request", () -> given(getUserSpec)
                        .when()
                        .get("/users?page2")
                        .then()
                        .spec(getUserResponseSpec)
                        .extract().response());

        step("Check response", () -> {
            List<Integer> actualId = response.jsonPath().getList("data.id");
            assertEquals(expectedId, actualId);
        });
    }

    @Test
    @DisplayName("Проверить, что у всех пользователей почта оканчивается на @regres.in")
    void compare() {
        String expectedEndOfEmail = "@reqres.in";
        List<UserDataModel> users =
                step("Make request", () -> given(getUserSpec)
                        .when()
                        .get("/users?page2")
                        .then()
                        .spec(getUserResponseSpec)
                        .extract().body().jsonPath().getList("data", UserDataModel.class));

        step("Check response", () ->
                assertTrue(users.stream().allMatch(e -> e.getEmail().endsWith(expectedEndOfEmail))));
    }
}