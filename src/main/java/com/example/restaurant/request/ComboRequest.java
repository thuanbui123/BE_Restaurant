package com.example.restaurant.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ComboRequest {
    @NotBlank(message = "Mã combo món ăn là một trường dữ liệu bắt buộc!")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Mã combo món ăn phải dài từ 6 đến 10 ký tự và không chứa khoảng trắng!")
    private String code;

    @NotBlank(message = "Tên combo món ăn là một trường dữ liệu bắt buộc!")
    private String name;

    @NotBlank(message = "Ảnh của combo món ăn là một trường dữ liệu bắt buộc!")
    private String img;

    @NotBlank(message = "Mô tả combo món ăn là một trường dữ liệu bắt buộc!")
    private String description;

    @NotNull(message = "Số lượng bán là một trường bắt buộc!")
    @Min(value = 1, message = "Số lượng bán phải lớn hơn 0!")
    private Integer soldCount;

    @NotBlank(message = "Ngày có hiệu lực bán combo món ăn là một trường bắt buộc!")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày có hiệu lực phải có định dạng yyyy-MM-dd")
    private String validFrom;

    @NotBlank(message = "Ngày hết hiệu lực bán combo món ăn là một trường bắt buộc!")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày hết hiệu lực phải có định dạng yyyy-MM-dd")
    private String validTo;
}
