package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.ReservingOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;


public interface ReservingRepository extends JpaSoftDeleteRepository<ReservingOrder, Integer> {
    @Query("SELECT s from ReservingOrder s where s.reservingTime >= :startOfDate and s.reservingTime <= :endOfDate order by s.reservingTime")
    List<ReservingOrder> findReservingOrdersBy(ZonedDateTime startOfDate, ZonedDateTime endOfDate);

    @Query("SELECT s from ReservingOrder s where s.reservingTime >= :startOfDate and s.reservingTime <= :endOfDate and s.deleted = false order by s.reservingTime")
    List<ReservingOrder> findAvailableReservingOrdersBy(ZonedDateTime startOfDate, ZonedDateTime endOfDate);

    @Modifying
    @Query("update ReservingOrder s set s.deleted = true where s.reservingTime < :overTime")
    void autoDeletedOrderOverTime(ZonedDateTime overTime);
}
