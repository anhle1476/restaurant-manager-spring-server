package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.AuthInfoDTO;
import com.codegym.restaurant.dto.UpdateAccountInfoDTO;
import com.codegym.restaurant.dto.UpdateAccountPasswordDTO;
import com.codegym.restaurant.service.StaffService;
import com.codegym.restaurant.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private StaffService staffService;

    @PostMapping("/update-password")
    public ResponseEntity<?> updateAccountPassword(
            @Valid @RequestBody UpdateAccountPasswordDTO dto,
            BindingResult result,
            Principal principal){
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);

        AuthInfoDTO authInfoDTO = appUtils.extractUserInfoFromToken(principal);
        Integer id = authInfoDTO.getId();

        staffService.updateAccountPassword(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/update-info")
    public ResponseEntity<?> updateAccountInfo(@Valid @RequestBody UpdateAccountInfoDTO updateAccountInfoDTO,
                                               BindingResult result,
                                               Principal principal) {
        if(result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        AuthInfoDTO authInfoDTO = appUtils.extractUserInfoFromToken(principal);
        Integer id = authInfoDTO.getId();
        staffService.updateAccountInfo(id,updateAccountInfoDTO);
        return new  ResponseEntity<>(updateAccountInfoDTO, HttpStatus.CREATED);
    }

}
