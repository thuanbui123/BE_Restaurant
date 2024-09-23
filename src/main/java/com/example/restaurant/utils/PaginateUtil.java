package com.example.restaurant.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            return ResponseEntity.ok().body(responsePage);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi truy vấn dữ liệu: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static  <T, R> ResponseEntity<?> findDistinctWithPaging(
            Function<Pageable, List<T>> dataFetcher,
            Pageable pageable,
            Function<T, R> mapper,
            Function<R, Comparable> sortKeyExtractor,
            boolean descending) {

        // Lấy tất cả các đối tượng
        List<T> allResponses = dataFetcher.apply(pageable);


        if (allResponses == null || allResponses.isEmpty()) {
            return new ResponseEntity<>("Không có dữ liệu", HttpStatus.NO_CONTENT);
        }

        // Ánh xạ và loại bỏ các mục trùng lặp
        Set<R> uniqueResponses = new HashSet<>(allResponses.stream()
                .map(mapper)
                .collect(Collectors.toList()));
        List<R> distinctResponses = new ArrayList<>(uniqueResponses);

        // Sắp xếp theo key được chỉ định
        Comparator<R> comparator = Comparator.comparing(sortKeyExtractor);
        if (descending) {
            comparator = comparator.reversed();
        }
        distinctResponses.sort(comparator);

        // Tính toán thông tin phân trang
        int start = Math.min((int) pageable.getOffset(), distinctResponses.size());
        int end = Math.min(start + pageable.getPageSize(), distinctResponses.size());
        List<R> pagedResponses = distinctResponses.subList(start, end);

        // Tạo đối tượng Page để trả về
        Page<R> page = new PageImpl<>(pagedResponses, pageable, distinctResponses.size());

        return ResponseEntity.ok(page);
    }
}
