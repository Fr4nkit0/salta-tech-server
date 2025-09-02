package com.saltaTech.common.application.exceptions.handler;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;
import com.saltaTech.common.application.exceptions.strategy.ExceptionHandlerStrategy;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ResourceNotFoundExceptionHandler implements ExceptionHandlerStrategy {
	@Override
	public ResponseEntity<ErrorResponse> handlerException(Exception exception, HttpServletRequest request,
														  HttpServletResponse response, LocalDateTime timestamp) {
		ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) exception;
		int httStatus = HttpStatus.NOT_FOUND.value();
		return ResponseEntity.status(httStatus)
				.body(new ErrorResponse(
						httStatus,
						request.getRequestURL().toString(),
						request.getMethod(),
						"Lo siento, no se pudo encontrar la información solicitada. "
								.concat("Por favor, revise la URL o intente otra búsqueda."),
						resourceNotFoundException.getMessage(),
						timestamp,
						null));
	}

	@Override
	public boolean canHandle(Exception exception) {
		return exception instanceof ResourceNotFoundException;
	}
}
