package com.codegym.restaurant.service;

import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.model.bussiness.BillDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface BillDetailService {
    List<BillDetail> getAllByBillId(Integer id);
    BillDetail getById(Integer id);
    BillDetail create(BillDetail billDetail);
    BillDetail update(BillDetail billDetail);
    void delete(Integer id);
}
