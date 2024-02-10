import constants.ErrorMessages;
import constants.TestData;
import dto.response.UserResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import dto.User;
import steps.UserSteps;

import java.util.Random;

public class CreateUserTests {
    private static final User USER = User.randomTestUser();
    private static final User USER_WITHOUT_EMAIL = new User(null, TestData.PASSWORD, TestData.NAME);
    private static final User USER_WITHOUT_PASSWORD = new User(
            TestData.LOGIN + new Random().nextInt() + TestData.MAIL,
            null,
            TestData.NAME
    );
    private static final User USER_WITHOUT_NAME = new User(
            TestData.LOGIN + new Random().nextInt() + TestData.MAIL,
            TestData.PASSWORD,
            null
    );
    private static UserResponse response;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
        response = UserSteps.createUser(USER, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", response.getSuccess());
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Запрос возвращает статус код - 200")
    public void createNewUser() {
        Assert.assertNotNull("Access Token не должен быть null", response.getAccessToken());
        Assert.assertNotNull("Refresh Token не должен быть null", response.getRefreshToken());
        Assert.assertEquals("Пользователи должны совпадать", USER, response.getUser());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Запрос возвращает статус код - 403 и сообщение User already exists")
    public void createUserAlreadyExists() {
        UserResponse alreadyExistsResponse = UserSteps.createUser(USER, 403);
        Assert.assertFalse("Запрос должен выполниться с ошибкой", alreadyExistsResponse.getSuccess());
        Assert.assertEquals(
                "Пользователь уже существует",
                ErrorMessages.USER_ALREADY_EXISTS,
                alreadyExistsResponse.getMessage()
        );
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля email")
    @Description("Запрос возвращает статус код - 403 и сообщение Email, password and name are required fields")
    public void checkNotCreateUserWithoutFillingEmail() {
        UserResponse response = UserSteps.createUser(USER_WITHOUT_EMAIL, 403);
        Assert.assertFalse("Запрос должен выполниться с ошибкой", response.getSuccess());
        Assert.assertEquals(
                "Обязательное поле email должно быть заполнено",
                ErrorMessages.NOT_FIELD_REQUIRED_FIELDS,
                response.getMessage()
        );
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля password")
    @Description("Запрос возвращает статус код - 403 и сообщение Email, password and name are required fields")
    public void checkNotCreateUserWithoutFillingPassword() {
        UserResponse response = UserSteps.createUser(USER_WITHOUT_PASSWORD, 403);
        Assert.assertFalse("Запрос должен выполниться с ошибкой", response.getSuccess());
        Assert.assertEquals(
                "Обязательное поле password должно быть заполнено",
                ErrorMessages.NOT_FIELD_REQUIRED_FIELDS,
                response.getMessage()
        );
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля name")
    @Description("Запрос возвращает статус код - 403 и сообщение Email, password and name are required fields")
    public void checkNotCreateUserWithoutFillingName() {
        UserResponse response = UserSteps.createUser(USER_WITHOUT_NAME, 403);
        Assert.assertFalse("Запрос должен выполниться с ошибкой", response.getSuccess());
        Assert.assertEquals(
                "Обязательное поле name должно быть заполнено",
                ErrorMessages.NOT_FIELD_REQUIRED_FIELDS,
                response.getMessage()
        );
    }

    @AfterClass
    public static void tearDown() {
        UserSteps.deleteUser(response.getAccessToken());
    }
}
