package dto;

import constants.TestData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String email;
    private String password;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public static User randomTestUser() {
        return new User(
                TestData.LOGIN + new Random().nextInt() + TestData.MAIL,
                TestData.PASSWORD,
                TestData.NAME
        );
    }
}
