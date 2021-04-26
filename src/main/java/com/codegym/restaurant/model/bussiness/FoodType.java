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
public class FoodType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_name", unique = true)
    @Pattern(regexp = "^[\\pL .,0-9()_:&-]{2,50}$", message = "Loại món ăn phải chứa từ 2-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên món ăn không được trống")
    private String name;

    @OneToMany(mappedBy = "foodType")
    @JsonIgnore
    private List<Food> foods;

    private boolean refundable;

    private boolean deleted;

    @Override
    public String toString() {
        return "FoodType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", refundable=" + refundable +
                ", deleted=" + deleted +
                '}';
    }
}
