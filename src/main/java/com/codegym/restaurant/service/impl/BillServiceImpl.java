package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.UpdateBillDetailDTO;
import com.codegym.restaurant.exception.BillNotFoundException;
import com.codegym.restaurant.exception.DeleteBillFailException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public List<Bill> getAll(LocalDateTime firstTime, LocalDateTime lastTime) {
        return billRepository.findBillByMonthOrDate(firstTime, lastTime);
    }

    @Override
    public List<Bill> getAllBillPayTimeIsNull() {
        return billRepository.findBillByPayTimeIsNull();
    }

    @Override
    public Bill getById(String id) {
        return billRepository.findBillByUUID(id);
    }

    @Override
    public Bill ChangeTable(Integer id) {
        return null;
    }

    @Override
    public Bill create(Bill bill) {
        Bill bills = billRepository.checkBillByAppTableAndPayTime(bill.getAppTable().getId());
        if (bills != null) {
            throw new BillNotFoundException("Không thể tạo bill ở bàn này vì bàn này đã co bill");
        }
        bill.setStartTime(LocalDateTime.now());
        return billRepository.save(bill);
    }


    @Override
    public Bill update(Bill bill) {
        Bill oldBill = getById(bill.getId());
        Map<Integer, UpdateBillDetailDTO> billDetailDTOMap = new HashMap<>();
        Map<Integer, BillDetail> billDetails = bill.getBillDetails()
                .stream()
                .collect(Collectors.toMap(sd -> sd.getFood().getId(), Function.identity()));
        List<BillDetail> oldBillDetails = oldBill.getBillDetails();
        List<BillDetail> deleteBillDetail = new ArrayList<>();
        for (BillDetail oldBillDetail : oldBillDetails) {
            Integer foodId = oldBillDetail.getFood().getId();
            BillDetail newBill = billDetails.get(foodId);
            UpdateBillDetailDTO billDetailDTO;
            if (newBill == null) {
                if (oldBillDetail.getDoneQuantity() > 0) {
                    throw new RuntimeException("Không thể xóa món đã nấu xong");
                }
                deleteBillDetail.add(oldBillDetail);
            } else {
                // case: tang them so luong cua mon -> cap nhat lai thoi gian order cuoi cung
                if (newBill.getQuantity() > oldBillDetail.getQuantity())
                    oldBillDetail.setLastOrderTime(LocalDateTime.now());
                // case: so luong moi < so luong da lam xong -> quang loi
                if (newBill.getQuantity() < oldBillDetail.getDoneQuantity())
                    throw new DeleteBillFailException("Không thể giảm số lượng của món đã nấu xong");
                oldBillDetail.setQuantity(newBill.getQuantity());
            }
        }
        billDetailRepository.deleteAll(deleteBillDetail);
        oldBillDetails.removeAll(deleteBillDetail);
        //nhung doi tuong con lai la doi tuong them moi
        Collection<BillDetail> newBillDetail = billDetails.values();
        if (newBillDetail.size() > 0) {
            for (BillDetail bd : newBillDetail) {
                bd.setBill(oldBill);
                oldBillDetails.add(bd);
            }
            billDetailRepository.saveAll(newBillDetail);
        }
        return billRepository.save(oldBill);
    }

    @Override
    public Bill doPayment(Bill bill, Integer staffId) {
        Bill bills = billRepository.findBillByUUID(bill.getId()
        );
        if (bill.getPayTime() != null) {
            throw new DoPaymentFailException("bill này đã thanh toán không thể thanh toán lại");
        }
        List<BillDetail> billDetails = bills.getBillDetails();
        long total = 0;
        for (BillDetail bd : billDetails) {
            if (bd.getDoneQuantity() != bd.getQuantity()) {
                throw new DoPaymentFailException("Có món chưa ra hết không thể tính tiền, kiểm tra lại hoặc xóa món");
            }
            bd.setPricePerUnit(bd.getFood().getPrice());
            total += bd.getFood().getPrice() * bd.getQuantity();
        }
        Staff staff = staffRepository.findAvailableById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Không thể chọn nhân viên dã xóa"));

        bills.setPayTime(LocalDateTime.now());
        bills.setDiscount(bill.getDiscount());
        bills.setSurcharge(bill.getSurcharge());
        bills.setDiscountDescription(bill.getDiscountDescription());
        bills.setLastPrice(total + bill.getSurcharge() - bill.getDiscount());
        bills.setStaff(staff);
//        bills.setAppTable(bills.getAppTable().getParent()==null);
        //tach ban chua song
        return billRepository.save(bills);
    }

    @Override
    public BillDetail processBillDoneQuantity(String idBill, Integer foodId, Integer processQuantity) {


        return null;
    }

    @Override
    public void delete(Integer id) {
        Bill bills = billRepository.getOne(id);
        for (BillDetail billDetail : bills.getBillDetails()) {
            if (billDetail.getDoneQuantity() != 0) {
                throw new DeleteBillFailException("Món được nấu xong không thể xóa bill và bill detail này");
            }
            billDetailRepository.deleteById(billDetail.getId());
        }
        billRepository.delete(bills);
    }


}
