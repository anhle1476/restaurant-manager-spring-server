package com.codegym.restaurant.model.hr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Data
public class Violation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên vi phạm không thể trống")
    @Pattern(regexp = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_0-9 ():-]{4,50}$"
            , message = "Tên ca phải chứa từ 4-50 ký tự.")
    @Column(name = "violation_name", unique = true)
    private String name;

    @Min(value = 0 ,message = "Mức phạt không thể âm")
    @Max(value = 100, message = "Mức phạt đã vượt quá giới hạn")
    private int finesPercent;

    @OneToMany(mappedBy = "violation")
    @JsonIgnore
    private List<ScheduleDetail> scheduleDetails;

    @OneToMany(mappedBy = "violation")
    @JsonIgnore
    private List<ViolationDetail> violationDetails;

    private boolean deleted;

    @Override
    public String toString() {
        return "Violation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", finesPercent=" + finesPercent +
                ", deleted=" + deleted +
                '}';
    }
}
