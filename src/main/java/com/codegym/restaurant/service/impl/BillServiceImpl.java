package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.MonthReportDTO;
import com.codegym.restaurant.dto.ProcessFoodDTO;
import com.codegym.restaurant.exception.AppTableNotAParentException;
import com.codegym.restaurant.exception.BillDetailUpdateFailedException;
import com.codegym.restaurant.exception.BillInUsingTableException;
import com.codegym.restaurant.exception.BillNotFoundException;
import com.codegym.restaurant.exception.BillUpdateFailedException;
import com.codegym.restaurant.exception.DoPaymentFailedException;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.model.bussiness.AppTable;
import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.model.bussiness.BillDetail;
import com.codegym.restaurant.model.bussiness.Food;
import com.codegym.restaurant.model.hr.RoleCode;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.AppTableRepository;
import com.codegym.restaurant.repository.BillDetailRepository;
import com.codegym.restaurant.repository.BillRepository;
import com.codegym.restaurant.repository.StaffRepository;
import com.codegym.restaurant.service.AppTableService;
import com.codegym.restaurant.service.BillService;
import com.codegym.restaurant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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

    @Autowired
    private AppTableService appTableService;

    @Override
    public List<Bill> findBillByUUID(String id) {
        return billRepository.findBillByUUID(id);
    }

    @Override
    public List<Bill> getAllBillOfMonth(String monthOfYear) {
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
        Map<Food, Integer> foodQuantitySold = new HashMap<>();
        List<Bill> billList = getAllBillOfMonth(month);
        for (Bill bill : billList) {
            LocalDate date = bill.getPayTime().toLocalDate();
            // cong don tong bill cua ngay trong map
            incomeByDate.merge(date, bill.getLastPrice(), Long::sum);

            for (BillDetail billDetail : bill.getBillDetails()) {
                Integer quantitySold = foodQuantitySold.get(billDetail.getFood());
                if (quantitySold != null)
                    foodQuantitySold.put(billDetail.getFood(), quantitySold + billDetail.getQuantity());
                foodQuantitySold.put(billDetail.getFood(), billDetail.getQuantity());
            }
        }
        monthReportDTO.setTotalOfMonth(totalProceedsInTheMonth(month));
        monthReportDTO.setIncomeByDate(incomeByDate);
        monthReportDTO.setFoodQuantitySold(foodQuantitySold);
        return monthReportDTO;
    }

    @Override
    public Map<Integer, Bill> mapBillByTableId() {
        Map<Integer, Bill> billMapByTableId = new TreeMap<>();
        List<Bill> currentBills = getAllCurrentBills();
        for (Bill bill : currentBills) {
            billMapByTableId.put(bill.getAppTable().getId(), bill);
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
                .orElseThrow(() -> new BillNotFoundException("Hóa đơn không tồn tại"));
    }

    @Override
    public Bill create(Bill bill) {
        Integer tabledId = bill.getAppTable().getId();
        checkTableInUse(tabledId);
        AppTable table = appTableService.getById(tabledId);
        bill.setAppTable(table.getParent() != null ? table.getParent() : table);
        List<BillDetail> billDetails = bill.getBillDetails()
                .stream()
                .filter(detail -> detail.getQuantity() > 0)
                .collect(Collectors.toList());
        if (billDetails.isEmpty())
            throw new BillUpdateFailedException("Không thể tạo hóa đơn không có món nào");
        bill.setBillDetails(billDetails);
        Bill saved = billRepository.save(bill);
        for (BillDetail billDetail : billDetails) {
            billDetail.setBill(saved);
        }
        billDetailRepository.saveAll(billDetails);
        return saved;
    }

    @Override
    public Bill changeTable(String billId, Integer newTableId) {
        checkTableInUse(newTableId);
        Bill currentBill = getCurrentBill(billId);
        AppTable newTable = appTableService.getById(newTableId);
        if (newTable.getParent() != null)
            throw new AppTableNotAParentException("Không thể chuyển đến bàn đang gộp");
        currentBill.setAppTable(newTable);
        return billRepository.save(currentBill);
    }

    @Override
    public Bill update(Bill newBill) {
        Bill oldBill = getCurrentBill(newBill.getId());
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
                if (oldBillDetail.getDoneQuantity() > 0 && !oldBillDetail.getFood().getFoodType().isRefundable())
                    throw new BillUpdateFailedException("Không thể xóa món đã nấu xong");
                if (newBillDetail != null)
                    newBillDetailsMap.remove(foodId);
                deleteBillDetail.add(oldBillDetail);
            } else {
                // case 2: bill moi co ton tai mon -> cap nhat
                updateExistingBillDetail(oldBillDetail, newBillDetail);
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
        // xoa tong tien de bat buoc cap nhat lai thong tin truoc khi thanh toan
        oldBill.setLastPrice(0);
        return billRepository.save(oldBill);
    }

    private void updateExistingBillDetail(BillDetail oldBillDetail, BillDetail newBillDetail) {
        int newQuantity = newBillDetail.getQuantity();
        if (newQuantity < 0)
            throw new BillUpdateFailedException("Số lượng của món không thể âm");
        // tang them so luong cua mon -> cap nhat lai thoi gian order cuoi cung
        if (newQuantity > oldBillDetail.getQuantity())
            oldBillDetail.resetOrderTime();
        // so luong moi < so luong da lam xong
        if (newQuantity < oldBillDetail.getDoneQuantity()) {
            // case 1: mon an khong thuoc loai cho tra mon -> quang loi
            if (!oldBillDetail.getFood().getFoodType().isRefundable())
                throw new BillUpdateFailedException("Không thể giảm số lượng của món đã nấu xong (loại không thể trả)");
            // case 2: mon an thuoc loai cho tra mon ->  giam so luong doneQuantity = newQuantity
            oldBillDetail.setDoneQuantity(newQuantity);
        }
        oldBillDetail.setQuantity(newQuantity);
    }

    @Override
    public Bill preparePayment(Bill bill) {
        Bill paymentBill = getCurrentBill(bill.getId());

        List<BillDetail> billDetails = paymentBill.getBillDetails();
        long total = 0;
        for (BillDetail billDetail : billDetails) {
            billDetail.setPricePerUnit(billDetail.getFood().getPrice());
            total += billDetail.getFood().getPrice() * billDetail.getQuantity();
        }
        billDetailRepository.saveAll(billDetails);

        paymentBill.setDiscount(bill.getDiscount());
        paymentBill.setSurcharge(bill.getSurcharge());
        paymentBill.setDiscountDescription(bill.getDiscountDescription());
        paymentBill.setLastPrice(total + bill.getSurcharge() - bill.getDiscount());
        return billRepository.save(paymentBill);
    }

    @Override
    public Bill doPayment(String billId, Integer staffId) {
        Bill paymentBill = getCurrentBill(billId);

        if (paymentBill.getLastPrice() == 0)
            throw new DoPaymentFailedException("Hóa đơn chưa cập nhật tạm tính");

        List<BillDetail> billDetails = paymentBill.getBillDetails();
        for (BillDetail billDetail : billDetails) {
            if (billDetail.getDoneQuantity() != billDetail.getQuantity())
                throw new DoPaymentFailedException("Không thể thanh toán hóa đơn chưa ra hết món");
        }

        Staff staff = staffRepository.findAvailableById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Không thể chọn nhân viên đã xóa"));
        paymentBill.setStaff(staff);
        paymentBill.setPayTime(dateUtils.now());

        // tach ban dang gop neu co & luu bill
        appTableService.doSeparatingTableGroup(paymentBill.getAppTable());
        return billRepository.save(paymentBill);
    }

    @Override
    public void processBillDoneQuantity(ProcessFoodDTO processFoodDTO) {
        BillDetail billDetail = billDetailRepository.findBillDetailByBillIdAndFoodId(processFoodDTO.getBillId(), processFoodDTO.getFoodId())
                .orElseThrow(() -> new BillUpdateFailedException("Không tồn tại món trong hóa đơn này"));
        int doneQuantityResult = billDetail.getDoneQuantity() + processFoodDTO.getProcessQuantity();
        if (doneQuantityResult > billDetail.getQuantity())
            throw new BillDetailUpdateFailedException("Số lượng món hoàn thành không thể lớn hơn số lượng món được đặt");
        billDetail.setDoneQuantity(doneQuantityResult);
        billDetailRepository.save(billDetail);
    }

    @Override
    public void delete(String id) {
        Bill bill = getById(id);
        for (BillDetail billDetail : bill.getBillDetails()) {
            if (billDetail.getDoneQuantity() != 0)
                throw new BillUpdateFailedException("Không thể xóa hóa đơn đã ra món");
        }
        appTableService.doSeparatingTableGroup(bill.getAppTable());
        billRepository.delete(bill);
    }

    @Override
    public void forceDelete(String billId, Integer staffId) {
        Staff staff = staffRepository.findAvailableById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Không thể chọn nhân viên đã xóa"));
        if (staff.getRole().getCode() != RoleCode.ADMIN)
            throw new BillUpdateFailedException("Tài khoản không có quyền admin, không thể bắt buộc xóa hóa đơn");

        Bill bill = getCurrentBill(billId);
        appTableService.doSeparatingTableGroup(bill.getAppTable());
        billRepository.delete(bill);
    }

    private Bill getCurrentBill(String billId) {
        return billRepository.findBillByPayTimeIsNullAndId(billId)
                .orElseThrow(() -> new BillNotFoundException("Hóa đơn không tồn tại hoặc đã được thanh toán"));
    }

    private void checkTableInUse(Integer tabledId) {
        boolean checkResult = billRepository.existsByPayTimeIsNullAndAppTableId(tabledId);
        if (checkResult)
            throw new BillInUsingTableException("Bàn đang được sử dụng");
    }

}
