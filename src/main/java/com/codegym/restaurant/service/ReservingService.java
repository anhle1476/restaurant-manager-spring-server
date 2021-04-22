package com.codegym.restaurant.service;

import com.codegym.restaurant.model.bussiness.ReservingOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReservingService extends BaseService<ReservingOrder, Integer> {

  Map<LocalDateTime, List<ReservingOrder>> findReservingOrdersBy(LocalDate dateOrder);

}
