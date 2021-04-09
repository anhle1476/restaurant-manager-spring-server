package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class ViolationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Violation violation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SalaryDetail salaryDetail;

    private int finesPercent;

    private int numberOfViolations;

    private boolean deleted;

    @Override
    public String toString() {
        return "ViolationDetail{" +
                "id=" + id +
                ", violation=" + violation +
                ", finesPercent=" + finesPercent +
                ", numberOfViolations=" + numberOfViolations +
                ", deleted=" + deleted +
                '}';
    }
}
