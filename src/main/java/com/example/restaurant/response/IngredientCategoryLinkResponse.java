package com.example.restaurant.response;

import com.example.restaurant.response.admin.IngredientsAdminResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class IngredientCategoryLinkResponse {
    private String ingredientCategoryName;
    private List<IngredientsAdminResponse> ingredientsAdminResponses;

    public void sortIngredientsByCreateAt() {
        if (ingredientsAdminResponses != null) {
            List<IngredientsAdminResponse> ingredientsCopy = new ArrayList<>(ingredientsAdminResponses);
            Collections.sort(ingredientsCopy, Comparator.comparing(IngredientsAdminResponse::getCreateAt));
        }
    }
}
