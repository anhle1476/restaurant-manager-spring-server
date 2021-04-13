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
    public ResponseEntity<?> updateStaff( @PathVariable(value = "id") Integer id,
                                          @Valid @RequestBody Staff staff, BindingResult result) {
        if (result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        Staff staffs = staffService.getById(id);
        staffService.update(staffs);
        return new ResponseEntity<>(staffs, HttpStatus.CREATED);
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

    //admin cấp lại mật khẩu cho toàn bộ staff
    @PostMapping("/{id}/update-password")
    public ResponseEntity<Staff> updateStaffPassword(@PathVariable(value = "id") Integer id,
                                                     @Valid @RequestBody UpdateStaffPasswordDTO updateStaffPasswordDTO
            , BindingResult result) {
        Staff staff = staffService.getById(id);
        staffService.updateStaffPassword(updateStaffPasswordDTO);
        return new ResponseEntity<>(staff, HttpStatus.NO_CONTENT);
    }


    // nhan {currentPassword , newPassword} + (Principal -> id tai khoan hien tai)
    // xu ly o service
    // ma hoa currentPassword -> dang 255
    // lay Id va currentPassword -> tao 1 ham trong staffRepo: findByIdAndPassword(Integer id, String password)
    // neu tra ve null -> quang loi: xac nhan mat khau that bai (400)
    // neu nhu ham tren ma tra ve 1 doi tuowng -> mat khau hien tai la cua tai khoan dang dang nhap
    // dung doi tuong do -> set mat khau moi (encode) vo
}
