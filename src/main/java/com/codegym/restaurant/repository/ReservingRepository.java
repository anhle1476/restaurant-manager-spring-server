package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.ReservingOrder;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;



public interface ReservingRepository extends JpaSoftDeleteRepository<ReservingOrder, Integer> {
    @Query("SELECT s from ReservingOrder s where s.reservingTime >= :startOfDate and s.reservingTime <= :endOfDate")
    List<ReservingOrder> findReservingOrdersBy(ZonedDateTime startOfDate, ZonedDateTime endOfDate);
}
