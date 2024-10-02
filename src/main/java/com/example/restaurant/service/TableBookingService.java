package com.example.restaurant.service;

import com.example.restaurant.entity.EmbeddableId.OrderedTableId;
import com.example.restaurant.entity.OrderedEntity;
import com.example.restaurant.entity.OrderedTableEntity;
import com.example.restaurant.entity.TableBookingEntity;
import com.example.restaurant.entity.TablesEntity;
import com.example.restaurant.mapper.TableBookingMapper;
import com.example.restaurant.repository.*;
import com.example.restaurant.request.TableBookingRequest;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.TimeConvertUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableBookingService {
    @Autowired
    private TableBookingRepository repository;

    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderedRepository orderedRepository;

    @Autowired
    private OrderedTableRepository orderedTableRepository;

    private ResponseEntity<?> findByStatus (String status, Pageable pageable) {
        return PaginateUtil.paginate(
                pg -> repository.findByStatus(status, pageable),
                pageable,
                TableBookingMapper::mapToResponse
        );
    }

    private ResponseEntity<?> findByCustomerId (Integer id, Pageable pageable) {
        return PaginateUtil.paginate(
                pg -> repository.findByUserId(id, pageable),
                pageable,
                TableBookingMapper::mapToResponse
        );
    }

    private ResponseEntity<?> findOneById (Integer id) {
        TableBookingEntity entity = repository.findOneById(id);
        if (entity == null) {
            return ResponseEntity.badRequest().body("Lịch đặt bàn có mã: " + id + " không tồn tại!");
        } else {
            return ResponseEntity.ok().body(TableBookingMapper.mapToResponse(entity));
        }
    }

    public ResponseEntity<?> findData (String prefix, Integer page, Integer size, String query, Integer id) {
        if (prefix.equals("find-by-status") && query != null && id == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByStatus(query, pageable);
        } else if (prefix.equals("find-by-customer-id") && query == null && id != null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByCustomerId(id, pageable);
        } else if (prefix.equals("find-by-id") && query == null && id != null) {
            return findOneById(id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API không tồn tại!");
    }

    @Transactional
    public ResponseEntity<?> addData (TableBookingRequest request) {
        try {
            TableBookingEntity exists = repository.findByCustomerIdAndStatus(request.getCustomerId(), "Đã đặt bàn");
            if (exists != null) {
                return ResponseEntity.badRequest().body("Khách hàng đã đặt bàn tại nhà hàng!");
            }
            exists = repository.findByCustomerIdAndStatus(request.getCustomerId(), "Khách nhận bàn");
            if (exists != null) {
                return ResponseEntity.badRequest().body("Khách hàng đang dùng bữa tại nhà hàng!");
            }
            TableBookingEntity entity = TableBookingMapper.mapToEntity(request);
            entity = repository.save(entity);
            String date = TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt());
            String mailBody = "Kính gửi " + entity.getCustomer().getName() + ",\n\n" +
                    "Chúng tôi xin chân thành cảm ơn Quý khách đã lựa chọn nhà hàng của chúng tôi cho buổi tiệc sắp tới!\n" +
                    "Quý khách đã đặt bàn vào lúc: " + entity.getBookingTime() + " ngày " + date + ".\n" +
                    "Đội ngũ của chúng tôi rất háo hức được chào đón và phục vụ Quý khách. Chúng tôi sẽ chuẩn bị mọi thứ để mang đến cho Quý khách một trải nghiệm ẩm thực tuyệt vời.\n\n" +
                    "Nếu có bất kỳ yêu cầu nào hoặc cần điều chỉnh thời gian đặt bàn, xin vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn sàng hỗ trợ Quý khách.\n\n" +
                    "Trân trọng,\n" +
                    "Quán nhậu tự do,\n" +
                    "Miếng nào to thì gắp";
            emailService.sendEmail(entity.getCustomer().getEmail(), "Thông tin đặt bàn tại quán nhậu tự do", mailBody);
            return ResponseEntity.ok().body("Đặt bàn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> cancelData (Integer id, TableBookingRequest request) {
        try {
            if (!repository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại phiếu đặt bàn có mã: " + id);
            }
            TableBookingEntity exists = repository.findOneById(id);
            if (!exists.getStatus().equalsIgnoreCase("Đã đặt bàn")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không được hủy phiếu đặt bàn đã hoàn thành hoặc khách hàng đã đến nhận bàn!");
            }
            exists.setStatus("Đã hủy");
            exists.setNote(request.getNote());
            repository.save(exists);
            return ResponseEntity.ok().body("Hủy đặt bàn thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> checkInReservation (Integer tableBookingId, Integer tableId) {
        try {
            TableBookingEntity entity = repository.findOneById(tableBookingId);
            if(entity == null) {
                return ResponseEntity.badRequest().body("Không tồn tại đơn đặt bàn: " + tableBookingId);
            }
            if (entity.getTablesEntity().getId() != 1) {
                return ResponseEntity.badRequest().body("Khách hàng đã hoặc đang dùng bữa tại nhà hàng!");
            }
            if (!tablesRepository.existsById(tableId)) {
                return ResponseEntity.badRequest().body("Không tồn tại bàn ăn có mã: " + tableId + "!");
            }
            TablesEntity tablesEntity = tablesRepository.findOneById(tableId);
            if (tablesEntity.getStatus().equalsIgnoreCase("Đang phục vụ")) {
                return ResponseEntity.badRequest().body("Bàn ăn có mã: " + tableId + " đang phục vụ khách hàng!");
            }


            tablesEntity.setStatus("Đang phục vụ");
            tablesRepository.save(tablesEntity);
            entity.setStatus("Khách nhận bàn");
            entity.setTablesEntity(tablesEntity);


            OrderedEntity ordered = new OrderedEntity();
            List<TablesEntity> tablesEntities = new ArrayList<>();
            tablesEntities.add(tablesEntity);
            ordered.setTables(tablesEntities);
            ordered.setCustomer(entity.getCustomer());
            ordered.setStatus("Chờ xử lý");
            ordered = orderedRepository.save(ordered);
            OrderedTableEntity orderedTable = new OrderedTableEntity();
            OrderedTableId orderedTableId = new OrderedTableId(ordered.getId(), tableId);
            orderedTable.setId(orderedTableId);
            orderedTable.setOrdered(ordered);
            orderedTable.setTable(tablesRepository.findOneById(tableId));
            orderedTableRepository.save(orderedTable);
            repository.save(entity);
            return ResponseEntity.ok().body(tableId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> changeTable (Integer newTable, Integer tableBookingId) {
        try {
            TableBookingEntity tableBooking = repository.findOneById(tableBookingId);
            if (tableBooking == null) {
                return ResponseEntity.badRequest().body("Chuyển bàn thất bại!");
            }
            TablesEntity currentTableEntity = tableBooking.getTablesEntity();
            TablesEntity newTableEntity = tablesRepository.findOneById(newTable);
            if (newTableEntity == null) {
                return ResponseEntity.badRequest().body("Không tồn tại bàn ăn có mã: " + newTable);
            }
            if (!newTableEntity.getStatus().equalsIgnoreCase("Trống")) {
                return ResponseEntity.badRequest().body("Bàn ăn " + newTable + " đã được xếp!");
            }
            tableBooking.setTablesEntity(newTableEntity);
            repository.save(tableBooking);
            currentTableEntity.setStatus("Trống");
            tablesRepository.save(currentTableEntity);
            newTableEntity.setStatus("Đang phục vụ");
            tablesRepository.save(newTableEntity);
            return ResponseEntity.ok().body("Chuyển bàn thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public List<TableBookingEntity> findLateReservations (LocalTime thresholdTime) {
        return repository.findLateReservations(thresholdTime, "Đã đặt bàn");
    }

    public TableBookingEntity save (TableBookingEntity entity) {
        return repository.save(entity);
    }
}
