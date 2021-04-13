package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.StaffCreationDTO;
import com.codegym.restaurant.dto.UpdateStaffPasswordDTO;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.hr.Shift;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.service.StaffService;
import com.codegym.restaurant.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/staffs")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<Staff>> show(@RequestParam(name = "deleted", required = false) String deleted) {
        List<Staff> staffList = deleted == null || !deleted.equals("true")
                ? staffService.getAll()
                : staffService.getAllDeleted();
        return new ResponseEntity<>(staffList, HttpStatus.OK);
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
            throw new IdNotMatchException("Id không trùng khớp");
        return new ResponseEntity<>(staffService.update(staff), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable(value = "id") Integer id) {
        staffService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Staff> restore(@PathVariable(value = "id") Integer id) {
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
            throw new IdNotMatchException("Id không trùng khớp");
        staffService.updateStaffPassword(updateStaffPasswordDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
