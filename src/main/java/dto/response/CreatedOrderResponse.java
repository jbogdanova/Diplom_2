package dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatedOrderResponse {
    private OrderResponse order;
    private String name;
    private Boolean success;
    private String message;
}
