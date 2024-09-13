package com.example.restaurant.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

public class PaginateUtil {
    public static <E, R> ResponseEntity<?> paginate(Function<Pageable, Page<E>> queryFunction, Pageable pageable, Function<E, R> mapper){
        try {
            //Thực hiện truy vấn
            Page<E> pageResult = queryFunction.apply(pageable);

            // Kiểm tra nếu không có dữ liệu
            if (pageResult == null || pageResult.isEmpty()) {
                return new ResponseEntity<>("Không có dữ liệu", HttpStatus.NO_CONTENT);
            }

            // Chuyển đổi dữ liệu từ entity sang response
            Page<R> responsePage = pageResult.map(mapper);

            // Trả về kết quả nếu có dữ liệu
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi truy vấn dữ liệu: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
