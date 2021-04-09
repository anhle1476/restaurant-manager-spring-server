package com.codegym.restaurant.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authorities("ROLE_ADMIN")
                .build();

        UserDetails cashier = User.builder()
                .username("cashier")
                .password(passwordEncoder.encode("password"))
                .authorities("ROLE_CASHIER")
                .build();

        UserDetails chef = User.builder()
                .username("chef")
                .password(passwordEncoder.encode("password"))
                .authorities("ROLE_CHEF")
                .build();

        return new InMemoryUserDetailsManager(admin, chef, cashier);
    }
}
