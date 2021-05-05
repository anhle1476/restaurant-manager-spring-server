package com.codegym.restaurant.utils;

import com.codegym.restaurant.exception.InvalidDateInputException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@Component
public class DateUtils {
    public static final ZoneId TIME_ZONE = ZoneId.of("GMT+07:00");

    public LocalDate getCurrentDate() {
        return now().toLocalDate();
    }

    public ZonedDateTime now() {
        return ZonedDateTime.now(TIME_ZONE);
    }

    public LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new InvalidDateInputException("Ngày không đúng định dạng");
        }
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

    public LocalDate firstDateOfCurrentMonth() {
        return getFirstDateOfMonth(getCurrentDate());
    }

    public ZonedDateTime endOfDate(LocalDate date) {
        return date.atTime(LocalTime.MAX).atZone(TIME_ZONE);
    }

    public ZonedDateTime startOfMonth(String dateStr) {
        return startOfDate(getFirstDateOfMonth(dateStr));
    }

    public ZonedDateTime endOfMonth(String dateStr) {
        return endOfDate(getLastDateOfMonth(dateStr));
    }

    public Date dateFromNow(int amount, TemporalUnit unit) {
        return Date.from(now().plus(amount, unit).toInstant());
    }
}
