package com.codegym.restaurant.model.bussiness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class FoodType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "food_type_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "foodType")
    @JsonIgnore
    private List<Food> foods;

    private boolean refundable;

    private boolean deleted;
}
