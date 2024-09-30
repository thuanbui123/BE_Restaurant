package com.example.restaurant.service;

import com.example.restaurant.entity.OrderedTableEntity;
import com.example.restaurant.entity.EmbeddableId.OrderedTableId;
import com.example.restaurant.entity.TablesEntity;
import com.example.restaurant.mapper.OrderedTableMapper;
import com.example.restaurant.repository.OrderedTableRepository;
import com.example.restaurant.repository.TablesRepository;
import com.example.restaurant.request.OrderedTableRequest;
import com.example.restaurant.response.BillResponse;
import com.example.restaurant.response.OrderResponse;
import com.example.restaurant.response.OrderedTableResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedTableService {
    @Autowired
    private OrderedTableRepository repository;

    @Autowired
    private TablesRepository tablesRepository;

    private ResponseEntity<?> findOrderByTableId (Integer tableId) {
        List<OrderedTableEntity> entities = repository.findOrderByTableId(tableId);
        OrderedTableResponse linkResponses = OrderedTableMapper.mapToResponse(entities);
        return ResponseEntity.ok().body(linkResponses);
    }

    public OrderedTableEntity findOneByOrderedId (Integer orderedId) {
        return repository.findOneByOrderedId(orderedId);
    }

    public ResponseEntity<?> findData (String prefix, Integer tableId) {
        if (prefix.equals("find-order-by-table-id") && tableId != null) {
            return findOrderByTableId(tableId);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại");
    }

    public ResponseEntity<?> addData (OrderedTableRequest request) {
        try {
            OrderedTableId id = new OrderedTableId(request.getOrderedId(), request.getTableId());
            OrderedTableEntity exists = repository.findOneByTableIdAndOrderedId(request.getTableId(), request.getOrderedId());
            if (exists != null) {
                return ResponseEntity.badRequest().body("Đơn hàng đã hoặc đang được phục vụ tại hệ thống bàn ăn của nhà hàng!");
            }

            OrderedTableEntity entity = OrderedTableMapper.mapToEntity(request);

            if (entity == null) {
                return ResponseEntity.badRequest().body("Bàn ăn hoặc đơn hàng không tồn tại!");
            }


            TablesEntity tablesEntity = tablesRepository.findOneById(request.getTableId());
            if (tablesEntity.getStatus().equalsIgnoreCase("Đang phục vụ")) {
                return ResponseEntity.badRequest().body("Bàn ăn đang được phục vụ!");
            }
            entity.setId(id);
            repository.save(entity);
            tablesEntity.setStatus("Đang phục vụ");
            tablesRepository.save(tablesEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm hóa đơn vào bàn ăn thành công!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public void deleteById (OrderedTableId id) {
        repository.deleteById(id);
    }
}
