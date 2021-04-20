package com.codegym.restaurant.model.bussiness;

import com.codegym.restaurant.model.hr.Staff;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false)
    private LocalDateTime startTime;

    private LocalDateTime payTime;

    private long lastPrice;

    @ManyToOne
    private Staff staff;

    private long surcharge;

    private long discount;

    private String discountDescription;

    @ManyToOne
    private AppTable appTable;

    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER)
    private List<BillDetail> billDetails;

    private boolean deleted;

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
                ", deleted=" + deleted +
                '}';
    }
}
