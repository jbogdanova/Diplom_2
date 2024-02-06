package steps;

import dto.response.UserResponse;
import io.qameta.allure.Step;
import dto.User;

import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step("Создать уникального пользователя с логином {user.email}, паролем {user.password} и именем {user.name}")
    public static UserResponse createUser(User user, int statusCode) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(statusCode)
                .extract().response()
                .getBody().as(UserResponse.class);
    }

    @Step("Авторизация пользователя с логином {user.email} и паролем {user.password}")
    public static UserResponse loginUser(User user, int statusCode) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(statusCode)
                .extract().response()
                .getBody().as(UserResponse.class);
    }

    @Step("Обновить данные для авторизованного пользователя")
    public static UserResponse updateUserWithAuth(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(200)
                .extract().response()
                .getBody().as(UserResponse.class);
    }

    @Step("Обновить данные для неавторизованного пользователя")
    public static UserResponse updateUserWithoutAuth(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(401)
                .extract().response()
                .getBody().as(UserResponse.class);
    }

    @Step("Удалить пользователя")
    public static UserResponse deleteUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .extract().response()
                .getBody().as(UserResponse.class);
    }
}