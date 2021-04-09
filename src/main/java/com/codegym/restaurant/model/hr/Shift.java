package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "shift_name", unique = true)
    private String name;

    private LocalTime startTime;

    private LocalTime endTime;

    @OneToMany(mappedBy = "shift")
    @JsonIgnore
    private List<Schedule> schedules;

    private boolean deleted;

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", deleted=" + deleted +
                '}';
    }
}
