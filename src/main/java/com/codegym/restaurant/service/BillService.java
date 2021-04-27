package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.MonthReportDTO;
import com.codegym.restaurant.dto.ProcessFoodDTO;
import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.model.bussiness.BillDetail;

import java.util.List;
import java.util.Map;

public interface BillService {
    List<Bill> findBillByUUID(String id);
    List<Bill> getAllBillOfMonth(String monthAndYear);
    List<Bill> getAllBillOfDate(String date);
    List<Bill> getAllCurrentBills();
    Bill getById(String id);
    Bill create(Bill bill);
    Bill changeTable(Integer id);
    Bill update(Bill bill);
    void delete(String id);
    Bill doPayment(Bill bill,Integer staffId);
    void processBillDoneQuantity(ProcessFoodDTO processFoodDTO);
    Long totalProceedsInTheMonth(String monthOfYear);
    Long totalProceedsInTheDate(String dateOfMonth);
    MonthReportDTO monthReport(String month);
    Map<Integer,Bill> mapBillByTableId();
}
