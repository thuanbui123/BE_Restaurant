package com.example.restaurant.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Slugify {
    public static String toSlug(String input) {
        // Chuyển đổi ký tự có dấu thành không dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noDiacritics = pattern.matcher(normalized).replaceAll("");

        // Thay thế các ký tự không hợp lệ bằng dấu gạch ngang
        String slug = noDiacritics.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")  // Xóa ký tự đặc biệt
                .trim()                           // Loại bỏ khoảng trắng ở đầu và cuối
                .replaceAll("\\s+", "-");         // Thay thế khoảng trắng bằng dấu gạch ngang

        return slug;
    }
}
