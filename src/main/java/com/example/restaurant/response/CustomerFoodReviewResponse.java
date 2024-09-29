package com.example.restaurant.response;

import lombok.Data;

@Data
public class CustomerFoodReviewResponse {
    private String avatar;
    private String customerName;
    private String comment;
    private Integer quantityStars;
    private String postDate;
}
