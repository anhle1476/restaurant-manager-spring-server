package com.codegym.restaurant.model.hr;

import com.codegym.restaurant.model.bussiness.Bill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
public class Staff implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private String fullname;

    private String phoneNumber;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<ShiftStaff> shiftStaffs;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<SalaryDetail> salaryDetails;

    // kiem tra tai khoan la CASHIER / ADMIN truoc khi them bill
    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<Bill> bills;

    private boolean deleted;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // tra ve mang cac quyen cua user
        RoleCode code = role.getCode();
        String codeName = code.name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(codeName);
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !deleted;
    }

}
