package com.codegym.restaurant;

import com.codegym.restaurant.model.hr.Role;
import com.codegym.restaurant.model.hr.RoleCode;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.RoleRepository;
import com.codegym.restaurant.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

@SpringBootApplication
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

}
