package com.codegym.restaurant.utils;

import com.codegym.restaurant.exception.InvalidDateInputException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class DateUtils {
    public LocalDate getFirstDateOfMonth(String dateStr) {
        try {
            return parseYearMonth(dateStr).atDay(1);
        }catch (Exception e) {
            throw new InvalidDateInputException("Ngày tháng không đúng định dạng");
        }
    }

    public LocalDate getLastDateOfMonth(String dateStr) {
        try {
            return parseYearMonth(dateStr).atEndOfMonth();
        }catch (Exception e) {
            throw new InvalidDateInputException("Ngày tháng không đúng định dạng");
        }
    }

    private YearMonth parseYearMonth(String dateStr) {
        String[] dateParts = dateStr.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        return YearMonth.of(year, month);
    }

}
