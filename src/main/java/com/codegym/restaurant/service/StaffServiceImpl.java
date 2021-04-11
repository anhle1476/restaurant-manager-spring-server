package com.codegym.restaurant.service;

import com.codegym.restaurant.exception.StaffNotFoundException;
import com.codegym.restaurant.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements UserDetailsService {

    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return staffRepository.findByUsername(username).orElseThrow(
                () -> new StaffNotFoundException(String.format("Tài khoản %s không tồn tại", username)));
    }
}
