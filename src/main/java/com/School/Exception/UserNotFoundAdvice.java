package com.School.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.School.Exception.UserNotFoundException;
@RestControllerAdvice
@ControllerAdvice
public class UserNotFoundAdvice {

	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public String UserNotFoundHandling(UserNotFoundException u) {
		return u.getMessage();
	}
}
