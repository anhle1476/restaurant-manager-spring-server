package com.codegym.restaurant.utils;

import com.codegym.restaurant.exception.InvalidDateInputException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Date;

@Component
public class DateUtils {
    public LocalDate getFirstDateOfMonth(String dateStr) {
        try {
            return parseYearMonth(dateStr).atDay(1);
        } catch (Exception e) {
            throw new InvalidDateInputException("Ngày tháng không đúng định dạng");
        }
    }

    public LocalDate getLastDateOfMonth(String dateStr) {
        try {
            return parseYearMonth(dateStr).atEndOfMonth();
        } catch (Exception e) {
            throw new InvalidDateInputException("Ngày tháng không đúng định dạng");
        }
    }

//    public LocalDateTime getStartDate(LocalDate dateStr) {
//        try {
//            return startOfDate(dateStr);
//        } catch (Exception e) {
//            throw new InvalidDateInputException("Giờ không đúng định dạng");
//        }
//    }
//
//    public LocalDateTime getEndDate(LocalDate dateStr) {
//        try {
//            return endOfDate(dateStr);
//        } catch (Exception e) {
//            throw new InvalidDateInputException("Giờ không đúng định dạng");
//        }
//    }

    private YearMonth parseYearMonth(String dateStr) {
        String[] dateParts = dateStr.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        return YearMonth.of(year, month);
    }

    public LocalDateTime startOfDate(LocalDate date) {
        return date.atStartOfDay();
    }


    public LocalDateTime endOfDate(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

}
