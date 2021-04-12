package com.codegym.restaurant.controller;

import com.codegym.restaurant.security.PasswordConfig;
import com.codegym.restaurant.utils.AppUtils;
import com.codegym.restaurant.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShiftController {

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private PasswordValidator passwordValidator;


}
