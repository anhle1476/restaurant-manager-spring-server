package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.AreaNotFoundException;
import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.ShiftNotFoundException;
import com.codegym.restaurant.model.bussiness.Area;
import com.codegym.restaurant.model.hr.Shift;
import com.codegym.restaurant.repository.AreaRepository;
import com.codegym.restaurant.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaRepository areaRepository;

    @Override
    public List<Area> getAll() {
        return areaRepository.findAllAvailable();
    }

    @Override
    public List<Area> getAllDeleted() {
        return areaRepository.findAllDeleted();
    }

    @Override
    public Area getById(Integer integer) {
        return areaRepository.findAvailableById(integer)
                .orElseThrow(() -> new AreaNotFoundException("Khu vực này khổng tồn tại"));
    }

    @Override
    public Area create(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public Area update(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public void delete(Integer integer) {
        Area area = getById(integer);
        area.setDeleted(true);
        areaRepository.save(area);
    }

    @Override
    public void restore(Integer integer) {
        Area area = areaRepository.findById(integer)
                .orElseThrow(() -> new AreaNotFoundException("Khu vực này không tồn tại"));
        if (!area.getId().equals(integer)) {
            throw new EntityRestoreFailedException("Không thẻ phục hồi khu vực chưa xóa");
        }
        area.setDeleted(false);
        areaRepository.save(area);
    }
}
