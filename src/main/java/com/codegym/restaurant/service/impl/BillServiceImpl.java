package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.BillDetailNotFoundException;
import com.codegym.restaurant.exception.BillUpdateFailException;
import com.codegym.restaurant.exception.DoPaymentFailException;
import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.model.bussiness.BillDetail;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.BillDetailRepository;
import com.codegym.restaurant.repository.BillRepository;
import com.codegym.restaurant.repository.StaffRepository;
import com.codegym.restaurant.service.BillService;
import com.codegym.restaurant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
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
    public List<Bill> getAll(ZonedDateTime firstTime, ZonedDateTime lastTime) {
        return billRepository.findBillByMonthOrDate(firstTime, lastTime);
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
            throw new BillDetailNotFoundException("Không thể tạo bill ở bàn này vì bàn này đã co bill");
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

        bills.setPayTime(dateUtils.now());
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
    public BillDetail processBillDoneQuantity(String idBill, Integer foodId, Integer processQuantity) {


        return null;
    }

    @Override
    public void delete(String id) {
        Bill bills = getById(id);
        for (BillDetail billDetail : bills.getBillDetails()) {
            if (billDetail.getDoneQuantity() != 0)
                throw new BillUpdateFailException("Món được nấu xong không thể xóa bill và bill detail này");
        }
        billRepository.delete(bills);
    }
}
