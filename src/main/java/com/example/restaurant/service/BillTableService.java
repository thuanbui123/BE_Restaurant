package com.example.restaurant.service;

import com.example.restaurant.entity.BillTableEntity;
import com.example.restaurant.entity.EmbeddableId.BillTableId;
import com.example.restaurant.entity.TablesEntity;
import com.example.restaurant.mapper.BillTableMapper;
import com.example.restaurant.repository.BillTableRepository;
import com.example.restaurant.repository.TablesRepository;
import com.example.restaurant.request.BillTableRequest;
import com.example.restaurant.response.BillResponse;
import com.example.restaurant.response.BillTableResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillTableService {
    @Autowired
    private BillTableRepository repository;

    @Autowired
    private TablesRepository tablesRepository;

    private ResponseEntity<?> findBillByTableIdAndBillStatus (Integer tableId, String status) {
        List<BillTableEntity> entities = repository.findBillByTableId(tableId);
        BillTableResponse linkResponses = BillTableMapper.mapToResponse(entities);
        List<BillResponse> response = linkResponses.getBillResponses().stream()
                .filter(linkResponse -> linkResponse.getStatus().equalsIgnoreCase(status))
                .toList();
        linkResponses.setBillResponses(response);
        return ResponseEntity.ok().body(linkResponses);
    }

    public BillTableEntity findOneByBillId (Integer billId) {
        return repository.findOneByBillId(billId);
    }

    public ResponseEntity<?> findData (String prefix, Integer tableId, String status) {
        if (prefix.equals("find-bill-by-table-id-and-bill-status") && tableId != null && status != null) {
            return findBillByTableIdAndBillStatus(tableId, status);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại");
    }

    public ResponseEntity<?> addData (BillTableRequest request) {
        try {
            BillTableId id = new BillTableId(request.getBillId(), request.getTableId());
            BillTableEntity exists = repository.findOneByTableIdAndBillId(request.getTableId(), request.getBillId());
            if (exists != null) {
                return ResponseEntity.badRequest().body("Hóa đơn đã hoặc đang được phục vụ tại hệ thống bàn ăn của nhà hàng!");
            }

            BillTableEntity entity = BillTableMapper.mapToEntity(request);

            if (entity == null) {
                return ResponseEntity.badRequest().body("Bàn ăn hoặc hóa đơn không tồn tại!");
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
    public void deleteById (BillTableId id) {
        repository.deleteById(id);
    }
}
