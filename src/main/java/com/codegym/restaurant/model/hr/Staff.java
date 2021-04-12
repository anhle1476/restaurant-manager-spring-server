package com.codegym.restaurant.model.hr;

import com.codegym.restaurant.model.bussiness.Bill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
public class Staff implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[a-zA-Z0-9_]{4,25}$", message = "Tên người dùng phải chứa từ 4-25 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Tên người dùng không được trống")
    private String username;

    private String password;

    @Pattern(regexp = "^[\\pL ]{4,50}$", message = "Tên người dùng phải chứa từ 4-50 ký tự và không có ký tự đặc biệt")
    @NotBlank(message = "Họ và tên nhân viên không được trống")
    private String fullname;

    @Pattern(regexp = "^0[\\d]{9,10}$", message = "Số điện thoại phải chứa 10-11 số và bắt đầu bằng số 0")
    @NotBlank(message = "Số điện thoại không được trống")
    private String phoneNumber;

    @Positive(message = "Lương mỗi ca không được âm")
    private long salaryPerShift;

    @ManyToOne
    @NotNull(message = "Chức vụ không được để trống")
    private Role role;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<ScheduleDetail> scheduleDetails;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<SalaryDetail> salaryDetails;

    // kiem tra tai khoan la CASHIER / ADMIN truoc khi them bill
    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<Bill> bills;

    private boolean deleted;

    @Override
    @JsonIgnore
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
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !deleted;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salaryPerShift=" + salaryPerShift +
                ", role=" + role +
                ", deleted=" + deleted +
                '}';
    }
}
