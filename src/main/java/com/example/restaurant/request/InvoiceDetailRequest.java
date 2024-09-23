package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceDetailRequest {
    @NotNull(message = "Mã phiếu nhập nguyên liệu là trường bắt buộc!")
    @Min(value = 1, message = "Mã phiếu nhập nguyên liệu phải lớn hơn 0!")
    private Integer importInvoiceId;

    @NotBlank(message = "Code chi tiết phiếu nhập là một trường dữ liệu bắt buộc!")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Code chi tiết phiếu nhập phải dài từ 6 đến 10 ký tự và không chứa khoảng trắng!")
    private String code;

    @NotNull (message = "Danh sách nguyên liệu là trường bắt buộc")
    private List<IngredientDetailRequest> ingredientDetailRequests;
}
