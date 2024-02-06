import constants.ErrorMessages;
import constants.TestData;
import dto.Order;
import dto.User;
import dto.response.CreatedOrderResponse;
import dto.response.UserResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import steps.OrderSteps;
import steps.UserSteps;

public class GetOrdersTests {
    private static final User USER = User.randomTestUser();
    private static final Order ORDER = new Order(TestData.TEST_BURGER_INGREDIENTS);
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
        UserResponse userResponse = UserSteps.createUser(USER, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", userResponse.getSuccess());
        accessToken = userResponse.getAccessToken();
    }

    @Test
    @DisplayName("Получение заказа авторизованного пользователя")
    @Description("Запрос возвращает статус код - 200")
    public void getOrderWithAuth() {
        CreatedOrderResponse orderResponse = OrderSteps.createOrderWithAuth(ORDER, accessToken, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", orderResponse.getSuccess());
        CreatedOrderResponse getOrderResponse = OrderSteps.getOrderWithAuth(accessToken);
        Assert.assertTrue("Запрос должен выполниться успешно", getOrderResponse.getSuccess());
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    @Description("Запрос возвращает статус код - 401")
    public void getOrderWithoutAuth() {
        CreatedOrderResponse getOrderResponse = OrderSteps.getOrderWithoutAuth();
        Assert.assertFalse("Запрос не должен выполниться", getOrderResponse.getSuccess());
        Assert.assertEquals(
                "Пользователь не должен быть авторизован",
                ErrorMessages.GET_ORDER_WITHOUT_AUTH,
                getOrderResponse.getMessage()
        );
    }

    @AfterClass
    public static void tearDown() {
        UserSteps.deleteUser(accessToken);
    }
}