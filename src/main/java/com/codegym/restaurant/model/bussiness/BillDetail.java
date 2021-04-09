package com.codegym.restaurant.model.bussiness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Entity
@Data
public class BillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Bill bill;

    @ManyToOne
    private Food food;

    private int quantity;

    private int doneQuantity;

    private Long pricePerUnit;

    private LocalDateTime lastOrderTime;

    private boolean deleted;

    private void resetOrderTime() {
        lastOrderTime = LocalDateTime.now();
    }

    @PrePersist
    protected void onPrePersist() {
        resetOrderTime();
    }

    @PreUpdate
    protected void onUpdate() {
        resetOrderTime();
    }
}
