package dto.response;

import dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String accessToken;
    private String refreshToken;
    private Boolean success;
    private String message;
    private User user;
}
