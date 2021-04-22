package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.TableGroupingDTO;
import com.codegym.restaurant.exception.AppTableNotFoundException;
import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.model.bussiness.AppTable;
import com.codegym.restaurant.repository.AppTableRepository;
import com.codegym.restaurant.service.AppTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppTableServiceImpl implements AppTableService {
    @Autowired
    private AppTableRepository appTableRepository;


    @Override
    public List<AppTable> getAll() {
        return appTableRepository.findAllAvailable();
    }

    @Override
    public List<AppTable> getAllDeleted() {
        return appTableRepository.findAllDeleted();
    }

    @Override
    public AppTable getById(Integer integer) {
        return appTableRepository.findAvailableById(integer)
                .orElseThrow(() -> new AppTableNotFoundException("Bàn này không tồn tại"));
    }

    @Override
    public AppTable create(AppTable appTable) {
        return appTableRepository.save(appTable);
    }

    @Override
    public AppTable update(AppTable appTable) {
        return appTableRepository.save(appTable);
    }

    @Override
    public void delete(Integer integer) {
        AppTable appTable = getById(integer);
        appTable.setDeleted(true);
        appTableRepository.save(appTable);
    }

    @Override
    public void restore(Integer integer) {
        AppTable appTable = appTableRepository.findById(integer)
                .orElseThrow(() -> new AppTableNotFoundException("Bàn này không tồn tại"));
        if (!appTable.isDeleted()) {
            throw new EntityRestoreFailedException("Không thể khôi phục khi bàn chưa xóa");
        }
        appTable.setDeleted(false);
        appTableRepository.save(appTable);
    }

    @Override
    public List<AppTable> groupingTables(TableGroupingDTO tableGroupingDTO) {
        List<AppTable> appTableList = new ArrayList<>();

        AppTable appTableParent = getById(tableGroupingDTO.getParent());


        return appTableList;
    }
}
