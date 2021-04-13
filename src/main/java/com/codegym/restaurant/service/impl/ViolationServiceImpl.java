package com.codegym.restaurant.service.impl;

import com.codegym.restaurant.exception.EntityRestoreFailedException;
import com.codegym.restaurant.exception.ViolationNotFoundException;
import com.codegym.restaurant.model.hr.Violation;
import com.codegym.restaurant.repository.ViolationRepository;
import com.codegym.restaurant.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ViolationServiceImpl implements ViolationService {
    @Autowired
    private ViolationRepository violationRepository;

    @Override
    public List<Violation> getAll() {
        return violationRepository.findAllAvailable();
    }

    @Override
    public List<Violation> getAllDeleted() {
        return violationRepository.findAllDeleted();
    }

    @Override
    public Violation getById(Integer integer) {
        return violationRepository.findAvailableById(integer)
                .orElseThrow(() -> new ViolationNotFoundException("Vi phạm này không tồn tại."));
    }

    @Override
    public Violation create(Violation violation) {
        return violationRepository.save(violation);
    }

    @Override
    public Violation update(Violation violation) {
        return violationRepository.save(violation);
    }

    @Override
    public void delete(Integer integer) {
        Violation violation = getById(integer);
        violation.setDeleted(true);
        violationRepository.save(violation);
    }

    @Override
    public void restore(Integer integer) {
        Violation violation = violationRepository.findById(integer)
                .orElseThrow(() -> new ViolationNotFoundException("Vi phạm này không tồn tại."));
        if (!violation.isDeleted())
            throw new EntityRestoreFailedException("không thể phục hồi vi phạm chưa xóa");
        violation.setDeleted(false);
        violationRepository.save(violation);
    }
}
