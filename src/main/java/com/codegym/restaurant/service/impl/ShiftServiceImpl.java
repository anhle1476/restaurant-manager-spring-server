package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.StaffNotFoundException;
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
    public Shift getById(Integer integer) {
        return shiftRepository.findAvailableById(integer)
                .orElseThrow(() -> new StaffNotFoundException("Ca làm việc không tồn tại"));
    }

    @Override
    public Shift create(Shift shift) {
        shiftRepository.save(shift);
        return shift;
    }

    @Override
    public Shift update(Shift shift) {
        shiftRepository.save(shift);
        return shift;
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
                .orElseThrow(() -> new RuntimeException("Ca khong ton tai"));
        if (!shift.isDeleted())
            throw new RuntimeException("Khong phuc hoi duoc ca chua xoa");
        shift.setDeleted(false);
        shiftRepository.save(shift);
    }
}
