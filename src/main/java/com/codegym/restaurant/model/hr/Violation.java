package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Violation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "violation_name", unique = true)
    private String name;

    private int finesPercent;

    @OneToMany(mappedBy = "violation")
    @JsonIgnore
    private List<ShiftStaff> shiftStaffs;

    @OneToMany(mappedBy = "violation")
    @JsonIgnore
    private List<ViolationDetail> violationDetails;

    private boolean deleted;
}
