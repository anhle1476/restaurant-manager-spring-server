package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name")
    private String name;

    private long salaryPerShift;

    @Enumerated(EnumType.STRING)
    private RoleCode code;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<Staff> staffs;

    private boolean deleted;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salaryPerShift=" + salaryPerShift +
                ", code=" + code +
                ", deleted=" + deleted +
                '}';
    }
}

