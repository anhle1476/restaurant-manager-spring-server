package com.codegym.restaurant.model.bussiness;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
public class ReservingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Ngày đặt bàn không được để trống")
    private ZonedDateTime reservingTime;

    @Pattern(regexp = "^[\\pL 0-9&.,():+-]{2,50}$", message = "Tên khách hàng phải chứa từ 2-50 ký tự, bao gồm chữ, số hoặc các ký tự &.,():+-")
    @NotBlank(message = "Họ và tên khách hàng không được trống")
    private String customerName;

    @Pattern(regexp = "^0[\\d]{9,10}$", message = "Số điện thoại phải chứa 10-11 số và bắt đầu bằng số 0")
    @NotBlank(message = "Số điện thoại không được trống")
    private String customerPhoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reserve_table")
    private List<AppTable> appTables;

    private boolean deleted;

    @Override
    public String toString() {
        return "ReservingOrder{" +
                "id=" + id +
                ", reservingTime=" + reservingTime +
                ", customerName='" + customerName + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
