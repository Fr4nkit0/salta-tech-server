package com.saltaTech.common.application.exceptions.handler;

import com.saltaTech.common.application.exceptions.strategy.ExceptionHandlerStrategy;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Component
public class MethodArgumentTypeMismatchExceptionHandler implements ExceptionHandlerStrategy {
	@Override
	public ResponseEntity<ErrorResponse> handlerException(Exception exception, HttpServletRequest request,
														  HttpServletResponse response, LocalDateTime timestamp) {
		MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = (MethodArgumentTypeMismatchException) exception;
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		Object valueRejected = methodArgumentTypeMismatchException.getValue();
		String propertyName = methodArgumentTypeMismatchException.getName();
		return ResponseEntity.status(httpStatus)
				.body(new ErrorResponse(
						httpStatus,
						request.getRequestURL().toString(),
						request.getMethod(),
						"Solicitud no válida: El valor proporcionado "
								+ valueRejected + "no tiene el tipo esperado para " + propertyName,
						methodArgumentTypeMismatchException.getMessage(),
						timestamp,
						null));
	}

	@Override
	public boolean canHandle(Exception exception) {
		return exception instanceof MethodArgumentTypeMismatchException;
	}
}
