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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[\\pL ]{2,10}$", message = "Tên Ca phải chứa từ 2-10 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên Ca không được trống")
    @Column(name = "shift_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "shift")
    @JsonIgnore
    private List<Schedule> schedules;

    private boolean deleted;

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
