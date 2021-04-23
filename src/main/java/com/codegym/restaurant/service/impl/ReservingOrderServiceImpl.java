package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.ReservingOrderNotFoundException;
import com.codegym.restaurant.model.bussiness.ReservingOrder;
import com.codegym.restaurant.repository.ReservingRepository;
import com.codegym.restaurant.service.ReservingService;
import com.codegym.restaurant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Service
@Transactional
public class ReservingOrderServiceImpl implements ReservingService {

    @Autowired
    private ReservingRepository reservingRepository;

    @Autowired
    private DateUtils dateUtils;


    @Override
    public List<ReservingOrder> getAll() {
        return reservingRepository.findAllAvailable();
    }

    @Override
    public List<ReservingOrder> getAllDeleted() {
        return reservingRepository.findAllDeleted();
    }

    @Override
    public ReservingOrder getById(Integer integer) {
        return reservingRepository.findAvailableById(integer)
                .orElseThrow(() -> new ReservingOrderNotFoundException("Không tìm thấy khách hàng đặt bàn"));
    }

    @Override
    public ReservingOrder create(ReservingOrder reservingOrder) {
        return reservingRepository.save(reservingOrder);
    }

    @Override
    public ReservingOrder update(ReservingOrder reservingOrder) {
        return reservingRepository.save(reservingOrder);
    }

    @Override
    public void delete(Integer integer) {
        ReservingOrder reservingOrder = getById(integer);
        reservingOrder.setDeleted(true);
        reservingRepository.save(reservingOrder);
    }

    @Override
    public void restore(Integer integer) {
        ReservingOrder reservingOrder = reservingRepository.findById(integer).
                orElseThrow(() -> new ReservingOrderNotFoundException("Đơn đặt bàn này không tồn tại"));
        if (!reservingOrder.isDeleted()) {
            throw new EntityRestoreFailedException("Không thể phục hồi đơn đặt bàn chưa xóa");
        }
        reservingOrder.setDeleted(false);
        reservingRepository.save(reservingOrder);
    }


    @Override
    public List<ReservingOrder> findReservingOrdersBy(LocalDate dateOrder) {
        LocalDateTime startTimeDate = dateUtils.startOfDate(dateOrder);
        LocalDateTime endTimeDate = dateUtils.endOfDate(dateOrder);
        List<ReservingOrder> orderList = reservingRepository.findReservingOrdersBy(startTimeDate, endTimeDate);
//        Map<LocalDateTime, List<ReservingOrder>> localDateTimeListMap = new TreeMap<>();
//        for (ReservingOrder currentReserving : orderList) {
//            LocalDateTime currentTime = currentReserving.getReservingTime();
//            List<ReservingOrder> reservingOrderList = localDateTimeListMap.get(currentTime);
//            if (reservingOrderList == null) {
//                reservingOrderList = new ArrayList<>();
//                reservingOrderList.add(currentReserving);
//                localDateTimeListMap.put(currentTime, reservingOrderList);
//            }
//        }
        return orderList;
    }
}