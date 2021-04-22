package com.codegym.restaurant.model.bussiness;

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
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[\\pL 0-9.():-]{1,25}$", message = "Tên Khu vực phải chứa từ 1-25 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên khu vực không được để trống")
    @Column(name = "area_name")
    private String name;

    @OneToMany(mappedBy = "area")
    @JsonIgnore
    private List<AppTable> appTables;

    private boolean deleted;

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
