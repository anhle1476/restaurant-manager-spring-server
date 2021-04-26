package com.codegym.restaurant.model.bussiness;

import com.codegym.restaurant.model.hr.Staff;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Bill {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(updatable = false)
    private LocalDateTime startTime;

    private LocalDateTime payTime;

    private long lastPrice;

    @ManyToOne
    private Staff staff;

    @Min(value = 0,message = "surcharge không được nhập số âm")
    private long surcharge;

    @Min(value = 0,message = "discount không được nhập số âm")
    private long discount;

    @Pattern(regexp = "^[\\pL 0-9()_:-]{2,50}$", message = "Tên món ăn phải chứa từ 4-50 ký tự và không có ký tự đặc biệt")
    private String discountDescription;

    @ManyToOne
    @NotNull(message = "Bàn không được để trống")
    private AppTable appTable;

    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BillDetail> billDetails;

    @PrePersist
    protected void onPrePersist() {
        if (appTable.getParent() != null)
            throw new RuntimeException("Không thể đặt order cho bàn phụ được ghép");
        this.startTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", payTime=" + payTime +
                ", lastPrice=" + lastPrice +
                ", staff=" + staff +
                ", surcharge=" + surcharge +
                ", discount=" + discount +
                ", discountDescription='" + discountDescription + '\'' +
                ", appTable=" + appTable +
                '}';
    }
}
