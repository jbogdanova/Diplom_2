package dto.response;

import dto.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientResponse {
    private boolean success;
    private ArrayList<Ingredient> data;

    public Ingredient getByName(String name) {
        for (Ingredient ingredient : data) {
            if (name.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }
}
