package com.example.restaurant.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class FoodCategoryLinkResponse {
    private String foodCategoryName;
    private List<FoodsResponse> foodsResponses;

    public void sortIngredientsByCreateAt() {
        if (foodsResponses != null) {
            List<FoodsResponse> foodsResponseList = new ArrayList<>(foodsResponses);
            Collections.sort(foodsResponseList, Comparator.comparing(FoodsResponse::getCreateAt));
        }
    }
}
