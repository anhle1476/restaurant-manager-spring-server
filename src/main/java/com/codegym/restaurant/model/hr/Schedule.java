package com.codegym.restaurant.model.hr;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(uniqueConstraints=
@UniqueConstraint(columnNames = {"date", "shift_id"}))
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Ngày của lịch làm không được để trống")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    @NotNull(message = "Ca làm việc không được để trống")
    private Shift shift;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ScheduleDetail> scheduleDetails;

    @Column(length = 300)
    private String note;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", date=" + date +
                ", shift=" + shift +
                ", scheduleDetails=" + scheduleDetails +
                ", note='" + note + '\'' +
                '}';
    }
}
