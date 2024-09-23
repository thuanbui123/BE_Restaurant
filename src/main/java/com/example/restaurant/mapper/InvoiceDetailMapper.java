package com.example.restaurant.mapper;

import com.example.restaurant.entity.EmbeddableId.InvoiceDetailId;
import com.example.restaurant.entity.InvoiceDetailEntity;
import com.example.restaurant.request.IngredientDetailRequest;
import com.example.restaurant.request.InvoiceDetailRequest;
import com.example.restaurant.response.IngredientDetailResponse;
import com.example.restaurant.response.InvoiceDetailResponse;
import com.example.restaurant.response.InvoiceResponse;
import com.example.restaurant.service.ImportInvoiceService;
import com.example.restaurant.service.IngredientsService;
import com.example.restaurant.utils.TimeConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InvoiceDetailMapper {
    private static ImportInvoiceService importInvoiceService;

    private static IngredientsService ingredientsService;

    @Autowired
    InvoiceDetailMapper(ImportInvoiceService importInvoiceService, IngredientsService ingredientsService) {
        InvoiceDetailMapper.importInvoiceService = importInvoiceService;
        InvoiceDetailMapper.ingredientsService = ingredientsService;
    }

    public static List<InvoiceDetailEntity> mapToEntity (InvoiceDetailRequest request) {
        List<IngredientDetailRequest> ingredientDetailRequests = request.getIngredientDetailRequests();

        return ingredientDetailRequests.stream()
                .map(ingredientDetailRequest -> {
                    if (!importInvoiceService.existsById(request.getImportInvoiceId())) {
                        throw new IllegalArgumentException("Phiếu nhập nguyên liệu không tồn tại: " + request.getImportInvoiceId());
                    }
                    if (!ingredientsService.existsById(ingredientDetailRequest.getIngredientId())) {
                        throw new IllegalArgumentException("Nguyên liệu không tồn tại: " + ingredientDetailRequest.getIngredientId());
                    }
                    InvoiceDetailEntity entity = new InvoiceDetailEntity();
                    entity.setImportInvoiceEntity(importInvoiceService.findOneById(request.getImportInvoiceId()));
                    entity.setIngredientsEntity(ingredientsService.findById(ingredientDetailRequest.getIngredientId()));
                    entity.setCode(request.getCode());
                    entity.setNote(ingredientDetailRequest.getNote());
                    entity.setQuantity(ingredientDetailRequest.getQuantity());
                    entity.setUnit(ingredientDetailRequest.getUnit());
                    InvoiceDetailId id = new InvoiceDetailId(ingredientDetailRequest.getIngredientId(), request.getImportInvoiceId());
                    entity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    entity.setId(id);
                    return entity;
                }).toList();
    }

    public static InvoiceResponse mapToResponse (InvoiceDetailEntity entity) {
        InvoiceResponse response = new InvoiceResponse();
        response.setCode(entity.getCode());
        response.setImportInvoiceId(entity.getId().getImportInvoiceId());
        response.setEntryDate(TimeConvertUtil.convertLocalDateTimeToString(entity.getImportInvoiceEntity().getEntryDate()));
        return response;
    }

    public static InvoiceDetailResponse mapToDetailResponse (List<InvoiceDetailEntity> entities) {
        InvoiceDetailResponse response = new InvoiceDetailResponse();
        response.setCode(entities.get(0).getCode());
        response.setImportInvoiceId(entities.get(0).getImportInvoiceEntity().getId());
        response.setIngredient(
                entities.stream()
                        .map(entity -> {
                            IngredientDetailResponse ingredientDetailResponse = new IngredientDetailResponse();
                            ingredientDetailResponse.setIngredientName(entity.getIngredientsEntity().getName());
                            ingredientDetailResponse.setQuantity(entity.getQuantity());
                            ingredientDetailResponse.setUnit(entity.getUnit());
                            ingredientDetailResponse.setNote(entity.getNote());
                            return ingredientDetailResponse;
                        })
                        .toList()
        );
        return response;
    }
}
