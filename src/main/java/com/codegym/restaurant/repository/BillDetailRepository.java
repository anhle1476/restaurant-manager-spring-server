package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillDetailRepository extends JpaRepository<BillDetail,Integer> {
    @Query("SELECT b from BillDetail b where b.bill.id = :id")
    List<BillDetail> findAllByBillId(Integer id);

}
