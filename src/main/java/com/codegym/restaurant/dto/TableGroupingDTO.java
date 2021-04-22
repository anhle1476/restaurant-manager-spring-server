package com.codegym.restaurant.dto;

import lombok.Data;
import java.util.List;

@Data
public class TableGroupingDTO {
    private Integer childrenId;
    private List<Integer> tableId;
}
