package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.MonthReportDTO;
import com.codegym.restaurant.dto.ProcessFoodDTO;
import com.codegym.restaurant.exception.BillDetailCantUpdateException;
import com.codegym.restaurant.exception.BillDetailNotFoundException;
import com.codegym.restaurant.exception.BillUpdateFailException;
import com.codegym.restaurant.exception.DoPaymentFailException;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.model.bussiness.BillDetail;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.BillDetailRepository;
import com.codegym.restaurant.repository.BillRepository;
import com.codegym.restaurant.repository.StaffRepository;
import com.codegym.restaurant.service.BillService;
import com.codegym.restaurant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public List<Bill> findBillByUUID(String id) {
        return billRepository.findBillByUUID(id);
    }

    @Override
    public List<Bill> getAllBillOfMonth(String monthOfYear){
        LocalDateTime firstDateOfMonth = dateUtils.startOfDate(dateUtils.getFirstDateOfMonth(monthOfYear));
        LocalDateTime endDateOfMonth = dateUtils.endOfDate(dateUtils.getLastDateOfMonth(monthOfYear));
        return billRepository.findBillByMonthOrDate(firstDateOfMonth, endDateOfMonth);
    }

    @Override
    public List<Bill> getAllBillOfDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDate = dateUtils.startOfDate(localDate);
        LocalDateTime endOfDate = dateUtils.endOfDate(localDate);
        return billRepository.findBillByMonthOrDate(startOfDate, endOfDate);
    }

    @Override
    public Long totalProceedsInTheMonth(String monthOfYear) {
        List<Bill> billList = getAllBillOfMonth(monthOfYear);
        long total = 0;
        for (Bill bill : billList){
            total +=  bill.getLastPrice();
        }
        return total;
    }

    @Override
    public Long totalProceedsInTheDate(String dateOfMonth) {
        List<Bill> billList = getAllBillOfDate(dateOfMonth);
        long total = 0;
        for (Bill bill : billList){
            total +=  bill.getLastPrice();
        }
        return total;
    }

    @Override
    public MonthReportDTO monthReport(String month) {
        MonthReportDTO monthReportDTO = new MonthReportDTO();
        Map<LocalDate, Long> incomeByDate = new TreeMap<>();
        Map<Food,Integer> foodQuantitySold = new HashMap<>();
        List<Bill> billList = getAllBillOfMonth(month);
        for (Bill bill : billList){
            LocalDate date = bill.getPayTime().toLocalDate();
            incomeByDate.merge(date, bill.getLastPrice(), Long::sum);
//          if (currentIncome != null)
//              incomeByDate.put(date, currentIncome + bill.getLastPrice());
//          else
//              incomeByDate.put(date,bill.getLastPrice());

            for (BillDetail billDetail : bill.getBillDetails()){
                Integer quantitySold = foodQuantitySold.get(billDetail.getFood());
                if (quantitySold != null )
                    foodQuantitySold.put(billDetail.getFood(),quantitySold + billDetail.getQuantity());
                foodQuantitySold.put(billDetail.getFood(),billDetail.getQuantity());
            }
        }
        monthReportDTO.setTotalOfMonth(totalProceedsInTheMonth(month));
        monthReportDTO.setIncomeByDate(incomeByDate);
        monthReportDTO.setFoodQuantitySold(foodQuantitySold);
        return monthReportDTO;
    }

    @Override
    public Map<Integer, Bill> listBillByTableId() {
        Map<Integer,Bill> billMapByTableId = new TreeMap<>();
        List<Bill> billByPayTimeIsNull = billRepository.findBillByPayTimeIsNull();
        for (Bill bill : billByPayTimeIsNull){
            billMapByTableId.put(bill.getAppTable().getId(),bill);
        }
        return billMapByTableId;
    }

    @Override
    public List<Bill> getAllBillPayTimeIsNull() {
        return billRepository.findBillByPayTimeIsNull();
    }

    @Override
    public Bill getById(String id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new BillDetailNotFoundException("Hóa đơn không tồn tại"));
    }

    @Override
    public Bill ChangeTable(Integer id) {
        return null;
    }

    @Override
    public Bill create(Bill bill) {
        Bill currentBill = billRepository.checkBillByAppTableAndPayTime(bill.getAppTable().getId());
        if (currentBill != null)
            throw new BillDetailNotFoundException("Không thể tạo bill ở bàn này vì bàn này đã có bill");
        Bill saved = billRepository.save(bill);
        List<BillDetail> billDetails = bill.getBillDetails();
        for (BillDetail billDetail : billDetails) {
            billDetail.setBill(saved);
        }
        billDetailRepository.saveAll(billDetails);
        return saved;
    }

    @Override
    public Bill update(Bill newBill) {
        Bill oldBill = getById(newBill.getId());
        Map<Integer, BillDetail> newBillDetailsMap = newBill.getBillDetails()
                .stream()
                .collect(Collectors.toMap(sd -> sd.getFood().getId(), Function.identity()));

        List<BillDetail> oldBillDetails = oldBill.getBillDetails();
        List<BillDetail> deleteBillDetail = new ArrayList<>();
        for (BillDetail oldBillDetail : oldBillDetails) {
            Integer foodId = oldBillDetail.getFood().getId();
            BillDetail newBillDetail = newBillDetailsMap.get(foodId);
            if (newBillDetail == null || newBillDetail.getQuantity() == 0) {
                // case 1: bill moi khong ton tai mon nay -> dua vao list de chuan bi xoa
                if (oldBillDetail.getDoneQuantity() > 0)
                    throw new BillUpdateFailException("Không thể xóa món đã nấu xong");
                deleteBillDetail.add(oldBillDetail);
            } else {
                // case 2: bill moi co ton tai mon -> cap nhat
                // check 1: tang them so luong cua mon -> cap nhat lai thoi gian order cuoi cung
                if (newBillDetail.getQuantity() > oldBillDetail.getQuantity())
                    oldBillDetail.setLastOrderTime(LocalDateTime.now());
                // check 2: so luong moi < so luong da lam xong -> quang loi
                if (newBillDetail.getQuantity() < oldBillDetail.getDoneQuantity())
                    throw new BillUpdateFailException("Không thể giảm số lượng của món đã nấu xong");
                oldBillDetail.setQuantity(newBillDetail.getQuantity());
                // xoa billDetail da cap nhat xong o trong map
                newBillDetailsMap.remove(foodId);
            }
        }
        // xoa nhung billDetail cu
        billDetailRepository.deleteAll(deleteBillDetail);
        oldBillDetails.removeAll(deleteBillDetail);
        //nhung doi tuong con lai trong map la billDetail them moi
        Collection<BillDetail> newBillDetails = newBillDetailsMap.values();
        if (newBillDetails.size() > 0) {
            for (BillDetail newBillDetail : newBillDetails) {
                newBillDetail.setBill(oldBill);
                oldBillDetails.add(newBillDetail);
            }
            billDetailRepository.saveAll(newBillDetails);
        }
        return billRepository.save(oldBill);
    }

    @Override
    public Bill doPayment(Bill bill, Integer staffId) {
        Bill bills = getById(bill.getId());
        if (bill.getPayTime() != null)
            throw new DoPaymentFailException("Hóa đơn này đã thanh toán không thể thanh toán lại");

        List<BillDetail> billDetails = bills.getBillDetails();
        long total = 0;
        for (BillDetail billDetail : billDetails) {
            if (billDetail.getDoneQuantity() != billDetail.getQuantity())
                throw new DoPaymentFailException("Có món chưa ra hết không thể tính tiền, kiểm tra lại hoặc xóa món");
            billDetail.setPricePerUnit(billDetail.getFood().getPrice());
            total += billDetail.getFood().getPrice() * billDetail.getQuantity();
        }
        Staff staff = staffRepository.findAvailableById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Không thể chọn nhân viên đã xóa"));

        bills.setPayTime(LocalDateTime.now());
        bills.setDiscount(bill.getDiscount());
        bills.setSurcharge(bill.getSurcharge());
        bills.setDiscountDescription(bill.getDiscountDescription());
        bills.setLastPrice(total + bill.getSurcharge() - bill.getDiscount());
        bills.setStaff(staff);
//        bills.setAppTable(bills.getAppTable().getParent()==null);
        //tach ban chua xong
        return billRepository.save(bills);
    }

    @Override
    public void processBillDoneQuantity(ProcessFoodDTO processFoodDTO) {
        BillDetail billDetail = billDetailRepository.findBillDetailByBillIdAndFoodId(processFoodDTO.getBillId(),processFoodDTO.getFoodId())
                .orElseThrow(() -> new BillUpdateFailException("Bill Detail không tồn tại"));
        int doneQuantityResult = billDetail.getDoneQuantity() + processFoodDTO.getProcessQuantity();
        if (doneQuantityResult > billDetail.getQuantity())
            throw new BillDetailCantUpdateException("Số lượng món hoàn thành không thể lớn hơn số lượng món được đặt");
        billDetail.setDoneQuantity(doneQuantityResult);
        billDetailRepository.save(billDetail);
    }


    @Override
    public void delete(String id) {
        Bill bills = getById(id);
        for (BillDetail billDetail : bills.getBillDetails()) {
            if ( billDetail.getDoneQuantity() != billDetail.getQuantity())
                throw new BillUpdateFailException("Món được nấu xong không thể xóa bill và bill detail này");
        }
        billRepository.delete(bills);
    }
}
