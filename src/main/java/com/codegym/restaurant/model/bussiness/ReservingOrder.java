package com.codegym.restaurant.model.bussiness;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ReservingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime reservingTime;

    private String customerName;

    private String customerPhoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reserve_table")
    private List<AppTable> appTables;

    private boolean deleted;
}
