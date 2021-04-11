package com.codegym.restaurant.utils;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codegym.restaurant.dto.AuthInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class AppUtils {

	public ResponseEntity<?> mapErrorToResponse(BindingResult result) {
		List<FieldError> fieldErrors = result.getFieldErrors();
		Map<String, String> errors = new HashMap<>();
		for (FieldError fieldError : fieldErrors) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	public AuthInfoDTO extractUserInfoFromToken(Principal principal) {
		return (AuthInfoDTO) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	}

}
