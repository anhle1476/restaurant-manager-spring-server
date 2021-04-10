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
public class ScheduleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
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
