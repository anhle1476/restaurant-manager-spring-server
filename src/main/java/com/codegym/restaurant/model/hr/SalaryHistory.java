package com.codegym.restaurant.model.hr;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class SalaryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate firstDateOfMonth;

    @OneToMany(mappedBy = "salaryHistory", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SalaryDetail> salaryDetails;

    private boolean deleted;

    @Override
    public String toString() {
        return "SalaryHistory{" +
                "id=" + id +
                ", firstDateOfMonth=" + firstDateOfMonth +
                ", salaryDetails=" + salaryDetails +
                ", deleted=" + deleted +
                '}';
    }
}
