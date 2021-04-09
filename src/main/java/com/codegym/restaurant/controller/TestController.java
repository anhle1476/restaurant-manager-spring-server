package com.codegym.restaurant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<?> testResponse(Principal principal) {
        return new ResponseEntity<>("hello " + principal.getName(), HttpStatus.OK);
    }
}
