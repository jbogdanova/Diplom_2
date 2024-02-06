import constants.ErrorMessages;
import constants.TestData;
import dto.User;
import dto.response.UserResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import steps.UserSteps;

import java.util.Random;

public class ChangeDataUserTests {
    private static final User USER = User.randomTestUser();
    private static String accessToken;
    private static final User UPDATED_USER = new User(
            TestData.LOGIN + new Random().nextInt() + TestData.MAIL,
            TestData.UPDATED_PASSWORD,
            TestData.UPDATED_NAME
    );

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = TestData.BASE_URI;
        UserResponse response = UserSteps.createUser(USER, 200);
        Assert.assertTrue("Запрос должен выполниться успешно", response.getSuccess());
        accessToken = response.getAccessToken();
    }

    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    @Description("Запрос возвращает статус код - 200")
    public void changeDataUserWithAuth() {
        UserResponse updateResponse = UserSteps.updateUserWithAuth(UPDATED_USER, accessToken);
        Assert.assertTrue("Запрос должен выполниться успешно", updateResponse.getSuccess());
        Assert.assertEquals("Пользователи должны совпадать", UPDATED_USER, updateResponse.getUser());
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя")
    @Description("Запрос возвращает статус код - 401")
    public void changeDataUserWithoutAuth() {
        UserResponse updateResponse = UserSteps.updateUserWithoutAuth(UPDATED_USER);
        Assert.assertFalse("Запрос не должен выполниться", updateResponse.getSuccess());
        Assert.assertEquals(ErrorMessages.UNAUTHORIZED_USER, updateResponse.getMessage());
    }

    @AfterClass
    public static void tearDown() {
        UserSteps.deleteUser(accessToken);
    }
}
