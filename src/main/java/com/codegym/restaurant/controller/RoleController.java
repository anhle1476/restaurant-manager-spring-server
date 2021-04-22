package com.codegym.restaurant.controller;

import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.model.hr.Role;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.service.RoleService;
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
@RequestMapping("/api/v1/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<Role>> show(@RequestParam(name = "deleted", required = false) String deleted) {
        List<Role> roleList = deleted == null || !deleted.equals("true")
                ? roleService.getAll()
                : roleService.getAllDeleted();
        return new ResponseEntity<>(roleList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody Role role, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        Role role1 = modelMapper.map(role, Role.class);
        return new ResponseEntity<>(roleService.create(role1), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable(value = "id") Integer id,
            @Valid @RequestBody Role role,
            BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!role.getId().equals(id))
            throw new IdNotMatchException("Id không trùng khớp");
        return new ResponseEntity<>(roleService.update(role), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable(value = "id") Integer id) {
        roleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Staff> restore(@PathVariable(value = "id") Integer id) {
        roleService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{roleId}/staffs")
    public ResponseEntity<List<Staff>> AllStaffsOfRoleId(@PathVariable(value = "roleId") Integer roleId) {
        return new ResponseEntity<>(roleService.getAllStaffsOfRole(roleId), HttpStatus.OK);
    }
}
