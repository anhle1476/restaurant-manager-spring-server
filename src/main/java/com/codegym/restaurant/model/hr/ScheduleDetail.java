package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Data
@Table(uniqueConstraints=
@UniqueConstraint(columnNames = {"schedule_id", "staff_id"}))
public class ScheduleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    @JsonIgnore
    private Schedule schedule;

    @ManyToOne
    private Violation violation;

    private float overtimeHours;

    private boolean deleted;

    @Override
    public String toString() {
        return "ScheduleDetail{" +
                "id=" + id +
                ", staff=" + staff +
                ", violation=" + violation +
                ", overtimeHours=" + overtimeHours +
                ", deleted=" + deleted +
                '}';
    }
}
