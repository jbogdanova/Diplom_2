package steps;

import dto.Order;
import dto.response.CreatedOrderResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создать заказ под авторизованным пользователем")
    public static CreatedOrderResponse createOrderWithAuth(Order order, String accessToken, int statusCode) {
        return createOrderWithAuth(order, accessToken)
                .then()
                .statusCode(statusCode)
                .extract().response()
                .getBody().as(CreatedOrderResponse.class);
    }

    @Step("Создать заказ под авторизованным пользователем")
    public static Response createOrderWithAuth(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создать заказ под неавторизованным пользователем")
    public static CreatedOrderResponse createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(401)
                .extract().response()
                .getBody().as(CreatedOrderResponse.class);
    }

    @Step("Получить заказ авторизованного пользователя")
    public static CreatedOrderResponse getOrderWithAuth(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .then()
                .statusCode(200)
                .extract().response()
                .getBody().as(CreatedOrderResponse.class);
    }

    @Step("Получить заказ неавторизованного пользователя")
    public static CreatedOrderResponse getOrderWithoutAuth() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders")
                .then()
                .statusCode(401)
                .extract().response()
                .getBody().as(CreatedOrderResponse.class);
    }
}
