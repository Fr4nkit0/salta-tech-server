package com.saltaTech.common.application.exceptions.handler;

import com.saltaTech.common.application.exceptions.strategy.ExceptionHandlerStrategy;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class IllegalArgumentExceptionHandler implements ExceptionHandlerStrategy {
	@Override
	public ResponseEntity<ErrorResponse> handlerException(Exception exception,
														  HttpServletRequest request,
														  HttpServletResponse response,
														  LocalDateTime timestamp) {
		IllegalArgumentException ex = (IllegalArgumentException) exception;
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		return ResponseEntity.status(httpStatus)
				.body(new ErrorResponse(
						httpStatus,
						request.getRequestURL().toString(),
						request.getMethod(),
						"Solicitud inválida: " + ex.getMessage(),
						ex.getLocalizedMessage(),
						timestamp,
						null));
	}

	@Override
	public boolean canHandle(Exception exception) {
		return exception instanceof IllegalArgumentException;
	}
}
