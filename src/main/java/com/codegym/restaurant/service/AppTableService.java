package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.TableGroupingDTO;
import com.codegym.restaurant.model.bussiness.AppTable;

import java.util.List;

public interface AppTableService extends BaseService<AppTable, Integer> {

    List<AppTable> groupingTables(TableGroupingDTO tableGroupingDTO);
}
