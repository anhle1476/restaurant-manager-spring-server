package com.codegym.restaurant.model.bussiness;

import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.utils.DateUtils;
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
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
public class Bill {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(updatable = false)
    private ZonedDateTime startTime;

    private ZonedDateTime payTime;

    private long lastPrice;

    @ManyToOne
    private Staff staff;

    @Min(value = 0,message = "surcharge không được nhập số âm")
    private long surcharge;

    @Min(value = 0,message = "discount không được nhập số âm")
    private long discount;

    @Size(max = 50, message = "Mô tả giảm giá phải chứa từ 0-50 ký tự")
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
        this.startTime = ZonedDateTime.now(DateUtils.TIME_ZONE);
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
