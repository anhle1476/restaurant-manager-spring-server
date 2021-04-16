package com.codegym.restaurant.model.hr;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
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

    private LocalDate firstDateOfMonth;

    private float totalOvertimeHours;

    private long salary;

    @OneToMany(mappedBy = "salaryDetail", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ViolationDetail> violationDetails;

    private boolean deleted;

    @Override
    public String toString() {
        return "SalaryDetail{" +
                "id=" + id +
                ", staff=" + staff +
                ", numberOfShift=" + numberOfShift +
                ", firstDateOfMonth=" + firstDateOfMonth +
                ", totalOvertimeHours=" + totalOvertimeHours +
                ", salary=" + salary +
                ", violationDetails=" + violationDetails +
                ", deleted=" + deleted +
                '}';
    }
}
