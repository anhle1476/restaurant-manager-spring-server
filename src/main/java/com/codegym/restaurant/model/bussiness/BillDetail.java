package com.codegym.restaurant.model.bussiness;

import com.codegym.restaurant.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(uniqueConstraints=
@UniqueConstraint(columnNames = {"bill_id", "food_id"}))
public class BillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    @JsonIgnore
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    private int quantity;

    private int doneQuantity;

    private long pricePerUnit;

    private ZonedDateTime lastOrderTime;

    public void resetOrderTime() {
        lastOrderTime = ZonedDateTime.now(DateUtils.TIME_ZONE);
    }

    @PrePersist
    protected void onPrePersist() {
        resetOrderTime();
    }

    @Override
    public String toString() {
        return "BillDetail{" +
                "id=" + id +
                ", food=" + food +
                ", quantity=" + quantity +
                ", doneQuantity=" + doneQuantity +
                ", pricePerUnit=" + pricePerUnit +
                ", lastOrderTime=" + lastOrderTime +
                '}';
    }
}
