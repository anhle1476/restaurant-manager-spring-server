package com.codegym.restaurant.model.bussiness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class AppTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[\\pL 0-9.()-:]{1,20}$", message = "Tên bàn phải chứa 1-20 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên bàn không được trống")
    @Column(name = "app_table_name", unique = true)
    private String name;

    @ManyToOne
    private AppTable parent;



    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<AppTable> children;

    @OneToMany(mappedBy = "appTable")
    @JsonIgnore
    private List<Bill> bills;

    @ManyToMany(mappedBy = "appTables")
    @JsonIgnore
    private List<ReservingOrder> reservingOrders;

    private boolean deleted;

    @Override
    public String toString() {
        return "AppTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                ", deleted=" + deleted +
                '}';
    }
}
