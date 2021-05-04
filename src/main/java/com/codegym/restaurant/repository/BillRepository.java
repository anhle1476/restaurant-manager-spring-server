package com.codegym.restaurant.repository;

import com.codegym.restaurant.model.bussiness.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill,String> {
    @Query("SELECT b from Bill b where b.payTime is not null and b.id like %:id% ")
    List<Bill> findBillByUUID(String id);

    @Query("SELECT b from Bill b where b.payTime >= :firstTime and b.payTime <= :lastTime order by b.payTime")
    List<Bill> findBillByMonthOrDate(ZonedDateTime firstTime, ZonedDateTime lastTime);

    List<Bill> findBillByPayTimeIsNull();

    Optional<Bill> findBillByPayTimeIsNullAndId(String id);

    boolean existsByPayTimeIsNullAndAppTableId(Integer appTableId);
}
