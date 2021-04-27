package com.codegym.restaurant.dto;

import lombok.Data;
import java.util.Set;

@Data
public class TableGroupingDTO {
    private Integer parent;
    private Set<Integer> children;
}
