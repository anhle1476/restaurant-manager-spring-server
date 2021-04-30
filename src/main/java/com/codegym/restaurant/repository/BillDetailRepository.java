package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail,Integer> {
    @Query("SELECT b from BillDetail b where b.bill.id = :billId and b.food.id = :foodId")
    Optional<BillDetail> findBillDetailByBillIdAndFoodId(String billId, Integer foodId);

    @Query("update BillDetail b set b.doneQuantity = b.quantity")
    @Modifying
    void testPayment();
}
