package com.saltaTech.common.application.exceptions.handler;

import com.saltaTech.common.application.exceptions.strategy.ExceptionHandlerStrategy;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class DefaultExceptionHandler implements ExceptionHandlerStrategy {

	@Override
	public ResponseEntity<ErrorResponse> handlerException(Exception exception, HttpServletRequest request,
														  HttpServletResponse response, LocalDateTime timestamp) {
		int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
		return ResponseEntity.status(httpStatus)
				.body(new ErrorResponse(
						httpStatus,
						request.getRequestURL().toString(),
						request.getMethod(),
						"Lo siento, se produjo un error inesperado. Inténtalo de nuevo más tarde.",
						exception.getMessage(),
						timestamp,
						null));
	}

	@Override
	public boolean canHandle(Exception exception) {
		return true;
	}

}
