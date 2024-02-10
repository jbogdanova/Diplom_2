import constants.ErrorMessages;
import constants.TestData;
import dto.Order;
import dto.User;
import dto.response.CreatedOrderResponse;
import dto.response.IngredientResponse;
import dto.response.UserResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.*;
import steps.IngredientSteps;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

public class CreateOrderTests {
    private static final User USER = User.randomTestUser();
    private static final Order ORDER = new Order();
    private static final Order INCORRECT_ORDER = new Order(TestData.INCORRECT_HASH_INGREDIENTS);
    private static final Order EMPTY_ORDER = new Order();
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
        UserResponse userResponse = UserSteps.createUser(USER, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", userResponse.getSuccess());
        accessToken = userResponse.getAccessToken();
        String ingredientHash = IngredientSteps.getIngredientHash()
                .getByName(TestData.TEST_INGREDIENT_NAME).get_id();
        ORDER.setIngredients(List.of(ingredientHash));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами под авторизованным пользователем")
    @Description("Запрос возвращает статус код - 200")
    public void createOrderWithAuth() {
        CreatedOrderResponse orderResponse = OrderSteps.createOrderWithAuth(ORDER, accessToken, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", orderResponse.getSuccess());
        Assert.assertEquals(
                "Названия бургера должно быть " + TestData.TEST_BURGER_NAME,
                TestData.TEST_BURGER_NAME,
                orderResponse.getName()
        );
        Assert.assertNotNull("Номер заказа не должен быть null", orderResponse.getOrder().getNumber());
    }

    /**
     * Заказ не должен создаваться. Только авторизованные пользователи могут делать заказы.
     * Структура эндпоинтов не меняется, но нужно предоставлять токен при запросе к серверу в поле Authorization
     */
    @Test
    @DisplayName("Создание заказа под неавторизованным пользователем")
    @Description("Запрос возвращает статус код - 401")
    public void createOrderWithoutAuth() {
        CreatedOrderResponse orderResponse = OrderSteps.createOrderWithoutAuth(ORDER);
        Assert.assertFalse("Запрос не должен выполниться", orderResponse.getSuccess());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Запрос возвращает статус код - 400 и сообщение Ingredient ids must be provided")
    public void createOrderWithoutIngredients() {
        CreatedOrderResponse orderResponse = OrderSteps.createOrderWithAuth(EMPTY_ORDER, accessToken, 400);
        Assert.assertFalse("Запрос не должен выполниться", orderResponse.getSuccess());
        Assert.assertEquals(
                "Заказ не должен быть пустым",
                ErrorMessages.ORDER_WITHOUT_INGREDIENTS,
                orderResponse.getMessage()
        );
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Запрос возвращает статус код - 500")
    public void createOrderWithIncorrectHashOfIngredients() {
        Response orderResponse = OrderSteps.createOrderWithAuth(INCORRECT_ORDER, accessToken);
        Assert.assertEquals("Запрос должен вернуть 500 ошибку", 500, orderResponse.statusCode());
    }

    @AfterClass
    public static void tearDown() {
        UserSteps.deleteUser(accessToken);
    }
}
