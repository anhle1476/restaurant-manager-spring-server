package com.codegym.restaurant.repository;

import com.codegym.restaurant.dto.TableGroupingDTO;
import com.codegym.restaurant.model.bussiness.AppTable;

import java.util.List;

public interface AppTableRepository extends JpaSoftDeleteRepository<AppTable, Integer> {
}
