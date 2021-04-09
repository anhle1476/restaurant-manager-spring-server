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
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "discount_name", unique = true)
    private String name;

    private int percent;

    @OneToMany(mappedBy = "discount")
    @JsonIgnore
    private List<Bill> bills;

    private boolean deleted;
}
