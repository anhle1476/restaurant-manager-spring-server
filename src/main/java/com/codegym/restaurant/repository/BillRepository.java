package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill,String> {

    @Query("SELECT b from Bill b where b.payTime >= :firstTime and b.payTime <= :lastTime ")
    List<Bill> findBillByMonthOrDate(LocalDateTime firstTime, LocalDateTime lastTime);

    @Query("SELECT b from Bill b where b.payTime is not null")
    List<Bill> findBillByPayTimeIsNull();

    @Query("SELECT b from Bill b where b.appTable.id = :idTable and b.payTime is null ")
    Bill checkBillByAppTableAndPayTime(Integer idTable);
}