package com.saltaTech.common.application.exceptions.strategy;

import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface ExceptionHandlerStrategy {
	ResponseEntity<ErrorResponse> handlerException(Exception exception, HttpServletRequest request,
												   HttpServletResponse response, LocalDateTime timestamp);

	boolean canHandle(Exception exception);
}
