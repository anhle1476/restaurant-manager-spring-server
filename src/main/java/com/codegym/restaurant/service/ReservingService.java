package com.codegym.restaurant.service;

import com.codegym.restaurant.model.bussiness.ReservingOrder;

import java.time.LocalDate;
import java.util.List;

public interface ReservingService extends BaseService<ReservingOrder, Integer> {

    List<ReservingOrder> findReservingOrdersBy(LocalDate dateOrder);

    List<ReservingOrder> findTodayOrders();

    void autoDeletedOverTime();
}
