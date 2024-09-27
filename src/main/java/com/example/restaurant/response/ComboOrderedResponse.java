package com.example.restaurant.response;

import lombok.Data;

import java.util.List;

@Data
public class ComboOrderedResponse {
    private List<ComboOrderedDetailResponse> comboResponses;
}
