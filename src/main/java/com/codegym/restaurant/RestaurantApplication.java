package com.codegym.restaurant;

import com.codegym.restaurant.model.hr.Role;
import com.codegym.restaurant.model.hr.RoleCode;
import com.codegym.restaurant.model.hr.Staff;
import com.codegym.restaurant.repository.RoleRepository;
import com.codegym.restaurant.repository.StaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

@SpringBootApplication
public class RestaurantApplication  {
//    @Autowired
//    private StaffRepository staffRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        Role role = new Role();
//        role.setCode(RoleCode.ADMIN);
//        role.setName("Quan tri vien");
//        roleRepository.save(role);
//
//        Staff staff = new Staff();
//        staff.setUsername("admin");
//        staff.setFullname("Nguyen Van Min");
//        staff.setPhoneNumber("0124350922");
//        staff.setSalaryPerShift(100000);
//        staff.setPassword(passwordEncoder.encode("password"));
//        staff.setRole(role);
//        staffRepository.save(staff);
//
//    }
}
