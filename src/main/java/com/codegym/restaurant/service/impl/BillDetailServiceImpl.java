package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.model.bussiness.BillDetail;
import com.codegym.restaurant.repository.BillDetailRepository;
import com.codegym.restaurant.repository.BillRepository;
import com.codegym.restaurant.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BillDetailServiceImpl implements BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private BillRepository billRepository;
    @Override
    public List<BillDetail> getAllByBillId(Integer id) {
        return billDetailRepository.findAllByBillId(id);
    }

    @Override
    public BillDetail getById(Integer id) {
        return billDetailRepository.getOne(id);
    }

    @Override
    public BillDetail create(BillDetail billDetail) {
//        Bill bill = billRepository.getOne(billDetail.getId());
//        List<BillDetail> billDetails = bill.getBillDetails();
//        for (BillDetail bd: billDetails){
//            if(bd.getFood().getId() != billDetail.getFood().getId()){
//                return billDetailRepository.save(billDetail);
//            }else {
//
//            }
//        }
        return null;
    }

    @Override
    public BillDetail update(BillDetail billDetail) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
