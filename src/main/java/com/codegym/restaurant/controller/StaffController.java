package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.StaffCreationDTO;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.service.StaffService;
import com.codegym.restaurant.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    // nhan {currentPassword , newPassword} + (Principal -> id tai khoan hien tai)
    // xu ly o service
    // ma hoa currentPassword -> dang 255
    // lay Id va currentPassword -> tao 1 ham trong staffRepo: findByIdAndPassword(Integer id, String password)
    // neu tra ve null -> quang loi: xac nhan mat khau that bai (400)
    // neu nhu ham tren ma tra ve 1 doi tuowng -> mat khau hien tai la cua tai khoan dang dang nhap
    // dung doi tuong do -> set mat khau moi (encode) vo
}
