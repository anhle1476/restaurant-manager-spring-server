package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.MonthReportDTO;
import com.codegym.restaurant.dto.ProcessFoodDTO;
import com.codegym.restaurant.model.bussiness.Bill;

import java.util.List;
import java.util.Map;

public interface BillService {
    List<Bill> findBillByUUID(String id);

    List<Bill> getAllBillOfMonth(String monthAndYear);

    List<Bill> getAllBillOfDate(String date);

    List<Bill> getAllCurrentBills();

    Bill getById(String id);

    Bill create(Bill bill);

    Bill changeTable(String billId, Integer newTableId);

    Bill update(Bill bill);

    void delete(String id);

    void forceDelete(String billId, Integer staffId);

    Bill doPayment(String billId, Integer staffId);

    Bill preparePayment(Bill bill);

    void processBillDoneQuantity(ProcessFoodDTO processFoodDTO);

    Long totalProceedsInTheMonth(String monthOfYear);

    Long totalProceedsInTheDate(String dateOfMonth);

    MonthReportDTO monthReport(String month);

    Map<Integer, Bill> mapBillByTableId();
}
