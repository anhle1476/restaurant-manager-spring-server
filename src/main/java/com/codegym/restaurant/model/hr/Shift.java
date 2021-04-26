package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[\\pL 0-9():-]{1,30}$", message = "Tên ca phải chứa từ 1-30 ký tự, bao gồm chữ, số hoặc các ký tự ():-")
    @NotBlank(message = "Tên ca không được trống")
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
