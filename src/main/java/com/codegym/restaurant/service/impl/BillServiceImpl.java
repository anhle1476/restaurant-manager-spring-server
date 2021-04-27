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

import java.time.ZonedDateTime;
import javax.transaction.Transactional;
import java.time.LocalDate;
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
        ZonedDateTime firstDateOfMonth = dateUtils.startOfMonth(monthOfYear);
        ZonedDateTime endDateOfMonth = dateUtils.endOfMonth(monthOfYear);
        return billRepository.findBillByMonthOrDate(firstDateOfMonth, endDateOfMonth);
    }

    @Override
    public List<Bill> getAllBillOfDate(String date) {
        LocalDate localDate = dateUtils.parseDate(date);
        ZonedDateTime startOfDate = dateUtils.startOfDate(localDate);
        ZonedDateTime endOfDate = dateUtils.endOfDate(localDate);
        return billRepository.findBillByMonthOrDate(startOfDate, endOfDate);
    }

    @Override
    public Long totalProceedsInTheMonth(String monthOfYear) {
        return getSumOfBills(getAllBillOfMonth(monthOfYear));
    }

    @Override
    public Long totalProceedsInTheDate(String dateOfMonth) {
        return getSumOfBills(getAllBillOfDate(dateOfMonth));
    }

    private long getSumOfBills(List<Bill> bills) {
        return bills.stream().mapToLong(Bill::getLastPrice).sum();
    }

    @Override
    public MonthReportDTO monthReport(String month) {
        MonthReportDTO monthReportDTO = new MonthReportDTO();
        Map<LocalDate, Long> incomeByDate = new TreeMap<>();
        Map<Food,Integer> foodQuantitySold = new HashMap<>();
        List<Bill> billList = getAllBillOfMonth(month);
        for (Bill bill : billList){
            LocalDate date = bill.getPayTime().toLocalDate();
            // cong don tong bill cua ngay trong map
            incomeByDate.merge(date, bill.getLastPrice(), Long::sum);

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
    public Map<Integer, Bill> mapBillByTableId() {
        Map<Integer,Bill> billMapByTableId = new TreeMap<>();
        List<Bill> currentBills = getAllCurrentBills();
        for (Bill bill : currentBills){
            billMapByTableId.put(bill.getAppTable().getId(),bill);
        }
        return billMapByTableId;
    }

    @Override
    public List<Bill> getAllCurrentBills() {
        return billRepository.findBillByPayTimeIsNull();
    }

    @Override
    public Bill getById(String id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new BillDetailNotFoundException("Hóa đơn không tồn tại"));
    }

    @Override
    public Bill changeTable(Integer id) {
        return null;
    }

    @Override
    public Bill create(Bill bill) {
        Bill currentBill = billRepository.checkBillByAppTableAndPayTime(bill.getAppTable().getId());
        if (currentBill != null)
            throw new BillDetailNotFoundException("Bàn đã có sẵn hóa đơn, không thể tạo mới");
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
                    oldBillDetail.setLastOrderTime(dateUtils.now());
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
        Bill paymentBill = getById(bill.getId());
        if (bill.getPayTime() != null)
            throw new DoPaymentFailException("Hóa đơn này đã thanh toán không thể thanh toán lại");

        List<BillDetail> billDetails = paymentBill.getBillDetails();
        long total = 0;
        for (BillDetail billDetail : billDetails) {
            if (billDetail.getDoneQuantity() != billDetail.getQuantity())
                throw new DoPaymentFailException("Có món chưa ra hết không thể tính tiền, kiểm tra lại hoặc xóa món");
            billDetail.setPricePerUnit(billDetail.getFood().getPrice());
            total += billDetail.getFood().getPrice() * billDetail.getQuantity();
        }
        billDetailRepository.saveAll(billDetails);

        Staff staff = staffRepository.findAvailableById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Không thể chọn nhân viên đã xóa"));

        paymentBill.setPayTime(dateUtils.now());
        paymentBill.setDiscount(bill.getDiscount());
        paymentBill.setSurcharge(bill.getSurcharge());
        paymentBill.setDiscountDescription(bill.getDiscountDescription());
        paymentBill.setLastPrice(total + bill.getSurcharge() - bill.getDiscount());
        paymentBill.setStaff(staff);
//        bills.setAppTable(bills.getAppTable().getParent()==null);
        // TODO:  TACH BAN
        return billRepository.save(paymentBill);
    }

    @Override
    public void processBillDoneQuantity(ProcessFoodDTO processFoodDTO) {
        BillDetail billDetail = billDetailRepository.findBillDetailByBillIdAndFoodId(processFoodDTO.getBillId(),processFoodDTO.getFoodId())
                .orElseThrow(() -> new BillUpdateFailException("Không tồn tại món trong hóa đơn này"));
        int doneQuantityResult = billDetail.getDoneQuantity() + processFoodDTO.getProcessQuantity();
        if (doneQuantityResult > billDetail.getQuantity())
            throw new BillDetailCantUpdateException("Số lượng món hoàn thành không thể lớn hơn số lượng món được đặt");
        billDetail.setDoneQuantity(doneQuantityResult);
        billDetailRepository.save(billDetail);
    }


    @Override
    public void delete(String id) {
        Bill bill = getById(id);
        for (BillDetail billDetail : bill.getBillDetails()) {
            if ( billDetail.getDoneQuantity() != billDetail.getQuantity())
                throw new BillUpdateFailException("Không thể xóa hóa đơn đã ra món");
        }
        // TODO: TACH BAN
        billRepository.delete(bill);
    }

}
