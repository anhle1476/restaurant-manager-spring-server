package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.ShiftNotFoundException;
import com.codegym.restaurant.model.hr.Shift;
import com.codegym.restaurant.repository.ShiftRepository;
import com.codegym.restaurant.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;

    @Override
    public List<Shift> getAll() {
        return shiftRepository.findAllAvailable();
    }

    @Override
    public List<Shift> getAllDeleted() {
        return shiftRepository.findAllDeleted();
    }

    @Override
    public List<Shift> getAllWithBothDeletedStatus() {
        return shiftRepository.findAll();
    }

    @Override
    public Shift getById(Integer integer) {
        return shiftRepository.findAvailableById(integer)
                .orElseThrow(() -> new ShiftNotFoundException("Ca làm việc không tồn tại"));
    }

    @Override
    public Shift create(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Override
    public Shift update(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Override
    public void delete(Integer integer) {
        Shift shift = getById(integer);
        shift.setDeleted(true);
        shiftRepository.save(shift);
    }

    @Override
    public void restore(Integer integer) {
        Shift shift = shiftRepository.findById(integer)
                .orElseThrow(() -> new ShiftNotFoundException("Ca này không tồn tại"));
        if (!shift.isDeleted())
            throw new EntityRestoreFailedException("Không phục hồi khi đối tượng chưa xóa");
        shift.setDeleted(false);
        shiftRepository.save(shift);
    }

}
