package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.StaffCreationDTO;
import com.codegym.restaurant.dto.UpdateStaffPasswordDTO;
import com.codegym.restaurant.model.hr.Shift;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.service.StaffService;
import com.codegym.restaurant.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/staffs")
public class StaffController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffService staffService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<Staff>> findAll() {
        return new ResponseEntity<>(staffService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createStaff(@Valid @RequestBody StaffCreationDTO staffCreationDTO, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        Staff staff = modelMapper.map(staffCreationDTO, Staff.class);
        return new ResponseEntity<>(staffService.create(staff), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(
            @PathVariable(value = "id") Integer id,
            @Valid @RequestBody Staff staff,
            BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!staff.getId().equals(id))
            throw new RuntimeException("Khong trung id");
        // TODO: chinh lai loi thanh IdNotMatchExc
        return new ResponseEntity<>(staffService.update(staff), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable(value = "id") Integer id) {
        staffService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Staff> undo(@PathVariable(value = "id") Integer id) {
        staffService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/update-password")
    public ResponseEntity<?> updateStaffPassword(
            @PathVariable(value = "id") Integer id,
            @Valid @RequestBody UpdateStaffPasswordDTO updateStaffPasswordDTO,
            BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!updateStaffPasswordDTO.getStaffId().equals(id))
            throw new RuntimeException("Khong trung id");
        // TODO: chinh lai loi thanh IdNotMatchExc
        staffService.updateStaffPassword(updateStaffPasswordDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
