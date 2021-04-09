package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class SalaryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Staff staff;

    private int numberOfShift;

    private long salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SalaryHistory salaryHistory;

    @OneToMany(mappedBy = "salaryDetail", fetch = FetchType.EAGER)
    private List<ViolationDetail> violationDetails;

    private boolean deleted;

    @Override
    public String toString() {
        return "SalaryDetail{" +
                "id=" + id +
                ", staff=" + staff +
                ", numberOfShift=" + numberOfShift +
                ", salary=" + salary +
                ", deleted=" + deleted +
                '}';
    }
}
