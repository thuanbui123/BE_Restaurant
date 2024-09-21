package com.example.restaurant.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeConvertUtil {
    public static String convertTimestampToDate (Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(timestamp);
    }

    public static Timestamp convertDateToTimestamp (String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date) dateFormat.parse(dateString);
        return new Timestamp(date.getTime());
    }

    public static LocalDateTime convertDateToLocalDateTime (String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return date.atTime(7, 0);
    }

    public static String convertLocalDateTimeToString (LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(dateTimeFormatter);
    }
}
