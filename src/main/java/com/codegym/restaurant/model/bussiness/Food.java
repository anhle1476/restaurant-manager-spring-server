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
import java.util.List;

@Entity
@Data
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "food_name", unique = true)
    private String name;

    private long price;

    private String unit;

    @ManyToOne
    private FoodType foodType;

    @OneToMany(mappedBy = "food")
    @JsonIgnore
    private List<BillDetail> billDetails;

    private boolean available;

    private boolean deleted;
}
