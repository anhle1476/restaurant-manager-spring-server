package com.codegym.restaurant.validator;


import com.codegym.restaurant.model.hr.Staff;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class PasswordValidator implements Validator {
    private final Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9@#$%^&+=()_-]{8,20}$");;

    @Override
    public boolean supports(Class<?> clazz) {
        return Staff.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String password = ((Staff) target).getPassword();

        if (password == null) {
            errors.rejectValue("password",null, "Mật khẩu không được để trống");
            return;
        }

        boolean isValid = passwordPattern.matcher(password).matches();
        if (!isValid) {
            errors.rejectValue("password",null, "Mật khẩu phải từ 8-20 ký tự, bao gồm chữ, số và các ký tự @#$%^&+=()_-");
        }
    }
}
