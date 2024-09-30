package com.example.restaurant.mapper;

import com.example.restaurant.entity.BillEntity;
import com.example.restaurant.entity.ComboEntity;
import com.example.restaurant.entity.ComboOrderEntity;
import com.example.restaurant.entity.EmbeddableId.ComboOrderedId;
import com.example.restaurant.repository.OrderedRepository;
import com.example.restaurant.request.ComboOrderedDetailRequest;
import com.example.restaurant.request.ComboOrderedRequest;
import com.example.restaurant.response.ComboOrderedDetailResponse;
import com.example.restaurant.response.ComboOrderedResponse;
import com.example.restaurant.service.BillService;
import com.example.restaurant.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComboOrderedMapper {
    private static ComboService comboService;
    private static OrderedRepository orderedRepository;

    @Autowired
    ComboOrderedMapper (ComboService comboService, OrderedRepository orderedRepository) {
        ComboOrderedMapper.comboService = comboService;
        ComboOrderedMapper.orderedRepository = orderedRepository;
    }

    public static List<ComboOrderEntity> mapToEntity (ComboOrderedRequest request) {
        List<ComboOrderedDetailRequest> requests = request.getRequests();
        return requests.stream()
                .map(request1 -> {
                    if (!orderedRepository.existsById(request.getOrdered())) {
                        throw new IllegalArgumentException("Đơn hàng có id: " + request.getOrdered() + " không tồn tại!");
                    }
                    if (!comboService.existsById(request1.getComboId())) {
                        throw new IllegalArgumentException("Combo món ăn có id: " + request1.getComboId() + " không tồn tại!");
                    }
                    ComboOrderEntity entity = new ComboOrderEntity();
                    ComboEntity comboEntity = comboService.findOneById(request1.getComboId());
                    entity.setId(new ComboOrderedId(request1.getComboId(), request.getOrdered()));
                    entity.setOrdered(orderedRepository.findOneById(request.getOrdered()));
                    entity.setCombo(comboEntity);
                    entity.setQuantity(request1.getQuantity());
                    entity.setTotalPrice(request1.getQuantity() * comboEntity.getPrice());
                    return entity;
                })
                .toList();
    }

    public static ComboOrderedResponse mapToResponse (List<ComboOrderEntity> entities) {
        ComboOrderedResponse response = new ComboOrderedResponse();
        response.setComboResponses(entities.stream()
                .map(entity -> {
                    ComboOrderedDetailResponse detailResponse = new ComboOrderedDetailResponse();
                    detailResponse.setComboId(entity.getCombo().getId());
                    detailResponse.setQuantity(entity.getQuantity());
                    detailResponse.setComboName(entity.getCombo().getName());
                    detailResponse.setQuantity(entity.getQuantity());
                    detailResponse.setTotalPrice(entity.getTotalPrice());
                    return detailResponse;
                })
                .toList()
        );
        return response;
    }
}
