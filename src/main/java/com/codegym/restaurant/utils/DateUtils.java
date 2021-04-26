package com.codegym.restaurant.utils;

import com.codegym.restaurant.exception.InvalidDateInputException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class DateUtils {
    public static final ZoneId TIME_ZONE = ZoneId.of("GMT+07:00");

    public LocalDate getCurrentDate() {
        return now().toLocalDate();
    }

    public ZonedDateTime now() {
        return ZonedDateTime.now(TIME_ZONE);
    }

    public LocalDate getFirstDateOfMonth(LocalDate date) {
        return LocalDate.of(date.getYear(), date.getMonth(), 1);
    }

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

    private YearMonth parseYearMonth(String dateStr) {
        String[] dateParts = dateStr.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        return YearMonth.of(year, month);
    }

    public ZonedDateTime startOfDate(LocalDate date) {
        return date.atStartOfDay(TIME_ZONE);
    }


    public ZonedDateTime endOfDate(LocalDate date) {
        return date.atTime(LocalTime.MAX).atZone(TIME_ZONE);
    }

}
