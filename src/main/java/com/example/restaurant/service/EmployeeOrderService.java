package com.example.restaurant.service;

import com.example.restaurant.entity.*;
import com.example.restaurant.entity.EmbeddableId.BillOrderedId;
import com.example.restaurant.entity.EmbeddableId.ComboOrderedId;
import com.example.restaurant.entity.EmbeddableId.FoodOrderedId;
import com.example.restaurant.mapper.EmployeeOrderMapper;
import com.example.restaurant.repository.*;
import com.example.restaurant.request.CancelOrderRequest;
import com.example.restaurant.request.EmployeeOrderRequest;
import com.example.restaurant.response.EmployeeOrderResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeOrderService {
    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private OrderedRepository orderedRepository;

    @Autowired
    private FoodsRepository foodsRepository;

    @Autowired
    private FoodOrderedRepository foodOrderedRepository;

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private ComboOrderRepository comboOrderRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillOrderRepository billOrderRepository;

    @Transactional
    public ResponseEntity<?> callOrder (Integer tableId, Integer customerId, EmployeeOrderRequest request) {
        try {
            if (request.getComboOrder() == null && request.getFoodOrder() == null) {
                return ResponseEntity.badRequest().body("Không có sản phẩm được thêm vào đơn hàng!");
            }

            TablesEntity tablesEntity = null;
            if (tableId != null) {
                tablesEntity = tablesRepository.findOneById(tableId);
            }

            if (tableId != null && tablesEntity == null) {
                return ResponseEntity.badRequest().body("Bàn không tồn tại.");
            }

            CustomersEntity customersEntity = customersRepository.findOneById(customerId);

            OrderedEntity ordered = orderedRepository.findByCustomerIdAndStatus(customerId, "Chờ xử lý");
            if (ordered == null) {
                ordered = new OrderedEntity();
                ordered.setStatus("Chờ xử lý");

                if (customersEntity == null) {
                    return ResponseEntity.badRequest().body("Không tồn tại khách hàng có mã: " + customerId);
                }
                ordered.setCustomer(customersRepository.findOneById(customerId));

                if (tablesEntity != null) {
                    List<TablesEntity> tablesEntities = new ArrayList<>();
                    tablesEntities.add(tablesEntity);
                    ordered.setTables(tablesEntities);
                }

                ordered = orderedRepository.save(ordered);
            }

            if (request.getFoodOrder() != null) {
                FoodsEntity foodsEntity = foodsRepository.findOneById(request.getFoodOrder().getFoodId());
                if (foodsEntity == null) {
                    throw new IllegalArgumentException("Món ăn không tồn tại!");
                }
                FoodOrderedId foodOrderedId = new FoodOrderedId(ordered.getId(), foodsEntity.getId());
                if (foodOrderedRepository.existsById(foodOrderedId)) {
                    throw new IllegalArgumentException("Món ăn đã được thêm vào đơn hàng trước đó!");
                }
                FoodOrderedEntity foodOrdered = new FoodOrderedEntity();
                foodOrdered.setId(foodOrderedId);
                foodOrdered.setOrdered(ordered);
                foodOrdered.setFood(foodsEntity);
                foodOrdered.setQuantity(request.getFoodOrder().getQuantity());
                foodOrdered.setTotalPrice(request.getFoodOrder().getQuantity() * foodsEntity.getPrice());
                foodOrderedRepository.save(foodOrdered);
            }
            if (request.getComboOrder() != null) {
                ComboEntity comboEntity = comboRepository.findOneById(request.getComboOrder().getComboId());
                if (comboEntity == null) {
                    throw new IllegalArgumentException("Combo món ăn không tồn tại!");
                }
                ComboOrderedId comboOrderedId = new ComboOrderedId(comboEntity.getId(), ordered.getId());
                if (comboOrderRepository.existsById(comboOrderedId)) {
                    throw new IllegalArgumentException("Combo đã được thêm vào đơn hàng trước đó!");
                }
                ComboOrderEntity comboOrder = new ComboOrderEntity();
                comboOrder.setId(comboOrderedId);
                comboOrder.setOrdered(ordered);
                comboOrder.setCombo(comboEntity);
                comboOrder.setQuantity(request.getComboOrder().getQuantity());
                comboOrder.setTotalPrice(request.getComboOrder().getQuantity() * comboEntity.getPrice());
                comboOrderRepository.save(comboOrder);
            }

            return ResponseEntity.ok().body("Gọi món thành công.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateOrder (Integer orderId, EmployeeOrderRequest request) {
        try {
            if (!orderedRepository.existsById(orderId)) {
                return ResponseEntity.badRequest().body("Không tồn tại đơn hàng " + orderId +"!");
            }
            if (orderedRepository.findOneById(orderId).getStatus().equalsIgnoreCase("Chờ xử lý")) {
                if (request.getFoodOrder() != null) {
                    FoodOrderedId foodOrderedId = new FoodOrderedId(orderId, request.getFoodOrder().getFoodId());
                    if (foodOrderedRepository.findById(foodOrderedId) == null) {
                        return ResponseEntity.badRequest().body("Không tồn tại món ăn có mã là " + request.getFoodOrder().getFoodId() + " trong đơn hàng " + orderId);
                    }
                    FoodOrderedEntity foodOrdered = foodOrderedRepository.findById(foodOrderedId);
                    foodOrdered.setQuantity(request.getFoodOrder().getQuantity());
                    foodOrdered.setTotalPrice(request.getFoodOrder().getQuantity() * foodsRepository.findOneById(request.getFoodOrder().getFoodId()).getPrice());
                    foodOrderedRepository.save(foodOrdered);
                }
                if (request.getComboOrder() != null) {
                    ComboOrderedId comboOrderedId = new ComboOrderedId(request.getComboOrder().getComboId(), orderId);
                    if (!comboOrderRepository.existsById(comboOrderedId)) {
                        return ResponseEntity.badRequest().body("Không tồn tại combo món ăn có mã là: " + request.getComboOrder().getComboId() + " trong đơn hàng "+ orderId);
                    }
                    ComboOrderEntity comboOrder = comboOrderRepository.findOneById(comboOrderedId);
                    comboOrder.setQuantity(request.getComboOrder().getQuantity());
                    comboOrder.setTotalPrice(request.getComboOrder().getQuantity() * comboRepository.findOneById(request.getComboOrder().getComboId()).getPrice());
                    comboOrderRepository.save(comboOrder);
                }
                return ResponseEntity.ok().body("Cập nhật đơn hàng thành công.");
            }
            return ResponseEntity.badRequest().body("Không được cập nhật đơn hàng có trạng thái khác chờ xử lý!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteItemInOrder (Integer orderId, EmployeeOrderRequest request) {
        try {
            if (!orderedRepository.existsById(orderId)) {
                return ResponseEntity.badRequest().body("Không tồn tại đơn hàng có mã: " + orderId);
            }
            if (!orderedRepository.findOneById(orderId).getStatus().equalsIgnoreCase("Chờ xử lý")) {
                return ResponseEntity.badRequest().body("Không được cập nhật đơn hàng có trạng thái khác chờ xử lý!");
            }
            if (request.getFoodOrder() != null) {
                FoodOrderedId foodOrderedId = new FoodOrderedId(orderId, request.getFoodOrder().getFoodId());
                if (!foodOrderedRepository.existsById(foodOrderedId)) {
                    return ResponseEntity.badRequest().body("Không tồn tại món ăn có mã " + request.getFoodOrder().getFoodId() + " trong đơn hàng " + orderId);
                }
                foodOrderedRepository.deleteById(foodOrderedId);
            }
            if (request.getComboOrder() != null) {
                ComboOrderedId comboOrderedId = new ComboOrderedId(request.getComboOrder().getComboId(), orderId);
                if (!comboOrderRepository.existsById(comboOrderedId)) {
                    return ResponseEntity.badRequest().body("Không tồn tại combo món ăn có mã " + request.getComboOrder().getComboId() + " trong đơn hàng" + orderId);
                }
                comboOrderRepository.deleteById(comboOrderedId);
            }
            return ResponseEntity.ok().body("Cập nhật đơn hàng thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> orderPayment (Integer orderId) {
        try {
            if (!orderedRepository.existsById(orderId)) {
                return ResponseEntity.badRequest().body("Không tồn tại đơn hàng có mã " + orderId);
            }
            OrderedEntity ordered = orderedRepository.findOneById(orderId);
            if (ordered.getStatus().equalsIgnoreCase("Đã thanh toán")) {
                return ResponseEntity.badRequest().body("Đơn hàng " + orderId + " đã được thanh toán!");
            }
            if (ordered.getStatus().equalsIgnoreCase("Đã hủy")) {
                return ResponseEntity.badRequest().body("Đơn hàng " + orderId + " đã hủy!");
            }
            List<FoodOrderedEntity> foodOrderedEntities = foodOrderedRepository.findByOrderedId(orderId);
            AtomicLong totalPrice = new AtomicLong(0L);
            if (foodOrderedEntities != null && !foodOrderedEntities.isEmpty()) {
                for (FoodOrderedEntity foodOrdered : foodOrderedEntities) {
                    totalPrice.set(totalPrice.get() + foodOrdered.getTotalPrice());
                }
            }
            List<ComboOrderEntity> comboOrderEntities = comboOrderRepository.findByOrderedId(orderId);
            if (comboOrderEntities != null && !comboOrderEntities.isEmpty()) {
                for (ComboOrderEntity comboOrder : comboOrderEntities) {
                    totalPrice.set(totalPrice.get() + comboOrder.getTotalPrice());
                    Integer quantity = comboOrder.getCombo().getSoldCount();
                    if (quantity < comboOrder.getQuantity()) {
                        return ResponseEntity.badRequest().body("Combo " + comboOrder.getCombo().getName() + " không đủ số lượng để bán");
                    }
                    ComboEntity comboEntity = comboOrder.getCombo();
                    comboEntity.setSoldCount(comboEntity.getSoldCount() - comboOrder.getQuantity());
                    comboRepository.save(comboEntity);
                }
            }
            if (totalPrice.get() == 0L) {
                return ResponseEntity.badRequest().body("Đơn hàng chưa có sản phẩm!");
            }
            BillEntity bill = new BillEntity();
            bill.setStatus("Thanh toán thành công");
            bill.setEmployee(employeeRepository.findOneById(3));
            bill.setTotalPrice(totalPrice.get());

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

            Random random = new Random();
            int randomNumber = random.nextInt(10000); // Số ngẫu nhiên từ 0 đến 9999
            int randomHours = random.nextInt(5); // Cộng thêm từ 0 đến 4 giờ ngẫu nhiên

            LocalDateTime futureDate = now.plusHours(randomHours);
            String dateTimePart = futureDate.format(dateFormatter);
            String billCode = String.format("BILL-%s-%04d", dateTimePart, randomNumber);
            bill.setCode(billCode);
            BillEntity saveBill = billRepository.save(bill);
            ordered.setStatus("Đã thanh toán");
            orderedRepository.save(ordered);
            BillOrderedId billOrderedId = new BillOrderedId(saveBill.getId(), orderId);
            BillOrderedEntity billOrdered = new BillOrderedEntity();
            billOrdered.setId(billOrderedId);
            billOrdered.setBill(saveBill);
            billOrdered.setOrdered(ordered);
            billOrderRepository.save(billOrdered);
            EmployeeOrderResponse response = EmployeeOrderMapper.mapToResponse(bill, ordered, comboOrderEntities, foodOrderedEntities);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> cancelOrder (Integer orderId, CancelOrderRequest note) {
        try {
            if (!orderedRepository.existsById(orderId)) {
                return ResponseEntity.badRequest().body("Không tồn tại đơn hàng có mã " + orderId);
            }
            OrderedEntity ordered = orderedRepository.findOneById(orderId);
            if (!ordered.getStatus().equalsIgnoreCase("Chờ xử lý")) {
                return ResponseEntity.badRequest().body("Đơn hàng đã hoàn thành!");
            }
            ordered.setStatus("Đã hủy");
            ordered.setNote(note.getNote());
            orderedRepository.save(ordered);
            return ResponseEntity.ok().body("Đã hủy đơn hàng " + orderId + " thành công.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
