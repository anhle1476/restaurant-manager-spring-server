package com.codegym.restaurant.service;

import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.model.bussiness.BillDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface BillService {
    List<Bill> getAll(LocalDateTime firstTime, LocalDateTime lastTime);
    List<Bill> getAllBillPayTimeIsNull();
    Bill getById(String id);
    Bill create(Bill bill);
    Bill ChangeTable(Integer id);
    Bill update(Bill bill);
    void delete(Integer id);
    Bill doPayment(Bill bill,Integer staffId);
    BillDetail processBillDoneQuantity(String idBill,Integer foodId,Integer processQuantity);
}
