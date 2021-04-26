package com.codegym.restaurant.service.impl;

import ch.qos.logback.core.read.ListAppender;
import com.codegym.restaurant.dto.SalaryDifferenceDTO;
import com.codegym.restaurant.dto.TableGroupingDTO;
import com.codegym.restaurant.exception.AppTableNotFoundException;
import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.model.bussiness.AppTable;
import com.codegym.restaurant.model.hr.ScheduleDetail;
import com.codegym.restaurant.repository.AppTableRepository;
import com.codegym.restaurant.service.AppTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        AppTable appTableParent = getById(tableGroupingDTO.getParent());
        if (appTableParent.getParent() != null)
            throw new AppTableNotFoundException("Bàn này đã được ghép");
        Set<Integer> newGroupIdSet = tableGroupingDTO.getChildren();
        List<AppTable> currentChildren = appTableParent.getChildren();
        List<AppTable> deletedTable = new ArrayList<>();
        for (AppTable currentChild : currentChildren) {
            if (newGroupIdSet.contains(currentChild.getId())) {
                deletedTable.add(currentChild);
                currentChild.setParent(null);
            }
        }
        for (AppTable tableIsDelete : deletedTable) {
            tableIsDelete.setParent(null);
        }
        currentChildren.removeAll(deletedTable);
        appTableRepository.saveAll(deletedTable);
        List<AppTable> newTableList = appTableRepository.findAllById(tableGroupingDTO.getChildren());
        for (AppTable newTables : newTableList) {
            if (newTables.getParent() != null)
                throw new AppTableNotFoundException("Bàn này đã được ghép");
            newTables.setParent(appTableParent);
        }
        currentChildren.addAll(newTableList);
        appTableRepository.saveAll(newTableList);
        appTableRepository.save(appTableParent);
        return currentChildren;
        // duyet qua currentChidren -> kiem tra ban co ton tai trong set moi khong
        //      neu co -> ban nay da ok (ban co - co)
        //              xoa id ban trong newGroupIdSet
        //      neu khong -> ban nay da bi tach khoi group (ban co - khong)
        //              dua ban vao listRemove de xoa khoi currentChildren & set lai parent cua ban ve null
        //
        // for (listRemove)
        //      setParent(null) -> xoa parent trong ban con bi tach
        // currentChildren.removeAll(listRemove) -> xoa ban con bi tach khoi list cua ban chinh
        // saveAll(listRemove)
        //
        // set: con lai nhung id ban moi duoc gop vao -> dung repo.findAllById -> loi ve dong ban moi duoc gop (newTableList)
        // for (newTableList)
        //      neu ban moi gop co parent -> ban nay da duoc gop san -> quang loi
        //      setParent(ban chinh)
        // currentChildren.addAll(newTableList)
        // saveAll(newTableList)
        // save(ban chinh)
        // return getAll()
    }

    @Override
    public List<AppTable> separateTables(TableGroupingDTO tableGroupingDTO) {
        List<AppTable> groupTable = appTableRepository.findAllById(tableGroupingDTO.getChildren());
        for (AppTable currentChild : groupTable){
            currentChild.setParent(null);
            appTableRepository.save(currentChild);
        }
        appTableRepository.saveAll(groupTable);
        return groupTable;
    }
}
