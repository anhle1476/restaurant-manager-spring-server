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
        Map<String, Integer> foodQuantitySold = new HashMap<>();
        long totalOfMonth = 0;
        List<Bill> billList = getAllBillOfMonth(month);
        for (Bill bill : billList) {
            LocalDate date = bill.getPayTime().toLocalDate();
            // cong don tong bill cua ngay trong map
            incomeByDate.merge(date, bill.getLastPrice(), Long::sum);
            totalOfMonth += bill.getLastPrice();

            for (BillDetail billDetail : bill.getBillDetails()) {
                String foodName = billDetail.getFood().getName();
                Integer quantitySold = foodQuantitySold.get(foodName);
                if (quantitySold == null)
                    quantitySold = 0;
                foodQuantitySold.put(foodName, quantitySold + billDetail.getQuantity());
            }
        }
        monthReportDTO.setTotalOfMonth(totalOfMonth);
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
                .orElseThrow(() -> new BillNotFoundException("H??a ????n kh??ng t???n t???i"));
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
            throw new BillUpdateFailedException("Kh??ng th??? t???o h??a ????n kh??ng c?? m??n n??o");
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
            throw new AppTableNotAParentException("Kh??ng th??? chuy???n ?????n b??n ??ang g???p");
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
                    throw new BillUpdateFailedException("Kh??ng th??? x??a m??n ???? n???u xong");
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
            throw new BillUpdateFailedException("S??? l?????ng c???a m??n kh??ng th??? ??m");
        // tang them so luong cua mon -> cap nhat lai thoi gian order cuoi cung
        if (newQuantity > oldBillDetail.getQuantity())
            oldBillDetail.resetOrderTime();
        // so luong moi < so luong da lam xong
        if (newQuantity < oldBillDetail.getDoneQuantity()) {
            // case 1: mon an khong thuoc loai cho tra mon -> quang loi
            if (!oldBillDetail.getFood().getFoodType().isRefundable())
                throw new BillUpdateFailedException("Kh??ng th??? gi???m s??? l?????ng c???a m??n ???? n???u xong (lo???i kh??ng th??? tr???)");
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
            throw new DoPaymentFailedException("H??a ????n ch??a c???p nh???t t???m t??nh");

        List<BillDetail> billDetails = paymentBill.getBillDetails();
        for (BillDetail billDetail : billDetails) {
            if (billDetail.getDoneQuantity() != billDetail.getQuantity())
                throw new DoPaymentFailedException("Kh??ng th??? thanh to??n h??a ????n ch??a ra h???t m??n");
        }

        Staff staff = staffRepository.findAvailableById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Kh??ng th??? ch???n nh??n vi??n ???? x??a"));
        paymentBill.setStaff(staff);
        paymentBill.setPayTime(dateUtils.now());

        // tach ban dang gop neu co & luu bill
        appTableService.doSeparatingTableGroup(paymentBill.getAppTable());
        return billRepository.save(paymentBill);
    }

    @Override
    public void processBillDoneQuantity(ProcessFoodDTO processFoodDTO) {
        BillDetail billDetail = billDetailRepository.findBillDetailByBillIdAndFoodId(processFoodDTO.getBillId(), processFoodDTO.getFoodId())
                .orElseThrow(() -> new BillUpdateFailedException("Kh??ng t???n t???i m??n trong h??a ????n n??y"));
        int doneQuantityResult = billDetail.getDoneQuantity() + processFoodDTO.getProcessQuantity();
        if (doneQuantityResult > billDetail.getQuantity())
            throw new BillDetailUpdateFailedException("S??? l?????ng m??n ho??n th??nh kh??ng th??? l???n h??n s??? l?????ng m??n ???????c ?????t");
        billDetail.setDoneQuantity(doneQuantityResult);
        billDetailRepository.save(billDetail);
    }

    @Override
    public void delete(String id) {
        Bill bill = getById(id);
        for (BillDetail billDetail : bill.getBillDetails()) {
            if (billDetail.getDoneQuantity() != 0)
                throw new BillUpdateFailedException("Kh??ng th??? x??a h??a ????n ???? ra m??n");
        }
        appTableService.doSeparatingTableGroup(bill.getAppTable());
        billRepository.delete(bill);
    }

    @Override
    public void forceDelete(String billId, Integer staffId) {
        Staff staff = staffRepository.findAvailableById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Kh??ng th??? ch???n nh??n vi??n ???? x??a"));
        if (staff.getRole().getCode() != RoleCode.ADMIN)
            throw new BillUpdateFailedException("T??i kho???n kh??ng c?? quy???n admin, kh??ng th??? b???t bu???c x??a h??a ????n");

        Bill bill = getCurrentBill(billId);
        appTableService.doSeparatingTableGroup(bill.getAppTable());
        billRepository.delete(bill);
    }

    private Bill getCurrentBill(String billId) {
        return billRepository.findBillByPayTimeIsNullAndId(billId)
                .orElseThrow(() -> new BillNotFoundException("H??a ????n kh??ng t???n t???i ho???c ???? ???????c thanh to??n"));
    }

    private void checkTableInUse(Integer tabledId) {
        boolean checkResult = billRepository.existsByPayTimeIsNullAndAppTableId(tabledId);
        if (checkResult)
            throw new BillInUsingTableException("B??n ??ang ???????c s??? d???ng");
    }

}
