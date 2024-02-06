import constants.ErrorMessages;
import constants.TestData;
import dto.User;
import dto.response.UserResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import steps.UserSteps;

public class UserLoginTests {
    private static final User USER = User.randomTestUser();
    private static final User USER_WITH_INCORRECT_LOGIN = new User(
            TestData.INCORRECT_LOGIN,
            USER.getPassword(),
            USER.getName()
    );
    private static final User USER_WITH_INCORRECT_PASSWORD = new User(
            USER.getEmail(),
            TestData.INCORRECT_PASSWORD,
            USER.getName()
    );
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
        UserResponse response = UserSteps.createUser(USER, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", response.getSuccess());
        accessToken = response.getAccessToken();
    }

    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    @Description("Запрос возвращает статус код - 200")
    public void loginUser() {
        UserResponse loginResponse = UserSteps.loginUser(USER, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", loginResponse.getSuccess());
        Assert.assertNotNull("Access Token не должен быть null", loginResponse.getAccessToken());
        Assert.assertNotNull("Refresh Token не должен быть null", loginResponse.getRefreshToken());
        Assert.assertEquals("Пользователи должны совпадать", USER, loginResponse.getUser());
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Запрос возвращает статус код - 401 и сообщение email or password are incorrect")
    public void loginUserWithIncorrectEmail() {
        UserResponse incorrectLoginResponse = UserSteps.loginUser(USER_WITH_INCORRECT_LOGIN, 401);
        Assert.assertFalse("Запрос должен выполниться с ошибкой", incorrectLoginResponse.getSuccess());
        Assert.assertEquals(
                "Должна быть ошибка данных",
                ErrorMessages.INCORRECT_DATA,
                incorrectLoginResponse.getMessage()
        );
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Запрос возвращает статус код - 401 и сообщение email or password are incorrect")
    public void loginUserWithIncorrectPassword() {
        UserResponse incorrectLoginResponse = UserSteps.loginUser(USER_WITH_INCORRECT_PASSWORD, 401);
        Assert.assertFalse("Запрос должен выполниться с ошибкой", incorrectLoginResponse.getSuccess());
        Assert.assertEquals(
                "Должна быть ошибка данных",
                ErrorMessages.INCORRECT_DATA,
                incorrectLoginResponse.getMessage()
        );
    }

    @AfterClass
    public static void tearDown() {
        UserSteps.deleteUser(accessToken);
    }
}