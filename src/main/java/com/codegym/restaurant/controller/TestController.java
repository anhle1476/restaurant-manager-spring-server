package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.AuthInfoDTO;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.BillDetailRepository;
import com.codegym.restaurant.utils.AppUtils;
import com.codegym.restaurant.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/test")
public class TestController {
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @GetMapping
    public ResponseEntity<?> testResponse(Principal principal) {
        AuthInfoDTO infoDTO = appUtils.extractUserInfoFromToken(principal);
        infoDTO.getId();
        return new ResponseEntity<>("hello " + principal.getName(), HttpStatus.OK);
    }

    @PostMapping("/test-register")
    public ResponseEntity<?> testRegister(@Valid @RequestBody Staff staff, BindingResult result) {
        passwordValidator.validate(staff, result);
        if(result.hasErrors()) {
            return appUtils.mapErrorToResponse(result);
        }
        return new ResponseEntity<>(staff, HttpStatus.CREATED);
    }

    @GetMapping("/pay")
    @Transactional
    public String testPay() {
        // TODO: remove after finish payment
        billDetailRepository.testPayment();
        return "ok";
    }
}
