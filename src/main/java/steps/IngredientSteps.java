package steps;

import dto.response.IngredientResponse;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class IngredientSteps {
    @Step("Получить данные об ингредиентах")
    public static IngredientResponse getIngredientHash() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/ingredients")
                .then()
                .statusCode(200)
                .extract().response()
                .getBody().as(IngredientResponse.class);
    }
}