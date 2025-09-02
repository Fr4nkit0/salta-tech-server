package com.saltaTech.common.application.controller;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;
import com.saltaTech.common.application.exceptions.factory.ExceptionHandlerFactory;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private final ExceptionHandlerFactory factory;

	public GlobalExceptionHandler(ExceptionHandlerFactory factory) {
		this.factory = factory;
	}

	@ExceptionHandler({
			Exception.class,
			ResourceNotFoundException.class,
			MethodArgumentTypeMismatchException.class,
			MethodArgumentNotValidException.class,
			IllegalArgumentException.class
	})
	public ResponseEntity<ErrorResponse> handleAllException(Exception exception, HttpServletRequest request,
															HttpServletResponse response) {

		ZoneId zona = ZoneId.of("America/Argentina/Buenos_Aires");
		LocalDateTime timestamp = LocalDateTime.now(zona);
		return factory.getHandler(exception, request, response, timestamp);

	}
}
