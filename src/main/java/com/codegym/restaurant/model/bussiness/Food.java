package com.codegym.restaurant.model.bussiness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "food_name", unique = true)
    @Pattern(regexp = "^[\\pL 0-9()_:-]{2,50}$", message = "Tên món ăn phải chứa từ 2-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên món không được trống")
    private String name;

    @Min(value = 0,message = "Giá không được âm")
    private long price;

    @Pattern(regexp = "^[\\pL 0-9()_:-]{1,50}$", message = "Tên đơn vị từ 1-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Đơn vị không được trống")
    private String unit;

    @ManyToOne
    @NotNull(message = "Loại món không được trống")
    private FoodType foodType;

    @OneToMany(mappedBy = "food")
    @JsonIgnore
    private List<BillDetail> billDetails;

    private String imageUrl;

    private boolean available;

    private boolean deleted;

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name +
                ", price=" + price +
                ", unit='" + unit +
                ", foodType=" + foodType +
                ", imageUrl='" + imageUrl +
                ", available=" + available +
                ", deleted=" + deleted +
                '}';
    }
}
