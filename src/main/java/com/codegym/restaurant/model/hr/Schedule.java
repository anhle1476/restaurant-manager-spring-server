package com.codegym.restaurant.model.hr;

import lombok.Data;

import javax.persistence.Column;
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
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;

    @ManyToOne
    private Shift shift;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ScheduleDetail> scheduleDetails;

    private boolean deleted;

    @Column(length = 300)
    private String note;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", date=" + date +
                ", shift=" + shift +
                ", scheduleDetails=" + scheduleDetails +
                ", deleted=" + deleted +
                ", note='" + note + '\'' +
                '}';
    }
}
