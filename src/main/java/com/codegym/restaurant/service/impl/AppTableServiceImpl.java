package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.dto.TableGroupingDTO;
import com.codegym.restaurant.exception.AppTableNotAParentException;
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
import java.util.Set;

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
        AppTable found = getById(appTable.getId());
        found.setName(appTable.getName());
        found.setArea(appTable.getArea());
        return appTableRepository.save(found);
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
        // lay ban chinh, kiem tra ban chinh co dang duoc ghep khong
        AppTable appTableParent = getById(tableGroupingDTO.getParent());
        if (appTableParent.getParent() != null)
            throw new AppTableNotFoundException("Bàn này đã được ghép");

        // lay danh sach ban con hien tai va ID ban con moi
        Set<Integer> newGroupIdSet = tableGroupingDTO.getChildren();
        List<AppTable> currentChildren = appTableParent.getChildren();

        // duyet qua danh sach ban con hien tai
        List<AppTable> removeFromGroupTables = new ArrayList<>();
        for (AppTable currentChild : currentChildren) {
            boolean isExistsInNewGroup = newGroupIdSet.contains(currentChild.getId());
            if (isExistsInNewGroup) {
                // neu ban ton tai trong group moi -> xoa khoi set ban moi
                newGroupIdSet.remove(currentChild.getId());
            } else {
                // neu ban khong ton tai trong group moi -> chuan bi xoa ban cu khoi list
                removeFromGroupTables.add(currentChild);
                currentChild.setParent(null);
            }
        }
        // xoa cac ban da bi loai khoi group moi
        currentChildren.removeAll(removeFromGroupTables);
        appTableRepository.saveAll(removeFromGroupTables);

        // cac ban con lai trong set la ban moi -> luu vao list ban con
        if (!newGroupIdSet.isEmpty()) {
            List<AppTable> newTableList = appTableRepository.findAllById(newGroupIdSet);
            for (AppTable newTables : newTableList) {
                if (isTableInGroup(newTables))
                    throw new AppTableNotFoundException("Bàn đã được ghép, không thể ghép với bàn khác");
                newTables.setParent(appTableParent);
            }
            currentChildren.addAll(newTableList);
            appTableRepository.saveAll(newTableList);
        }
        appTableRepository.save(appTableParent);
        return getAll();
    }

    private boolean isTableInGroup(AppTable table) {
        List<AppTable> children = table.getChildren();
        boolean isParentTable = children != null && !children.isEmpty();
        return table.getParent() != null || isParentTable;
    }

    @Override
    public List<AppTable> separateTableById(Integer parentTableId) {
        AppTable parentTable = getById(parentTableId);
        doSeparatingTableGroup(parentTable);
        return getAll();
    }

    @Override
    public void doSeparatingTableGroup(AppTable table) {
        if (table.getParent() != null)
            throw new AppTableNotAParentException("Không thể tách bàn không phải là bàn chính");
        List<AppTable> childrenTables = table.getChildren();
        if (childrenTables != null && !childrenTables.isEmpty()) {
            for (AppTable children : childrenTables) {
                children.setParent(null);
            }
            childrenTables.clear();
            appTableRepository.save(table);
        }
    }
}
