package com.saltaTech.common.application.exceptions.handler;

import com.saltaTech.common.application.exceptions.strategy.ExceptionHandlerStrategy;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MethodArgumentNotValidExceptionHandler implements ExceptionHandlerStrategy {
	@Override
	public ResponseEntity<ErrorResponse> handlerException(Exception exception, HttpServletRequest request,
														  HttpServletResponse response, LocalDateTime timestamp) {
		MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		List<ObjectError> errors = methodArgumentNotValidException.getAllErrors();
		List<String> details = errors.stream().map(error -> {
			if (error instanceof FieldError fieldError) {
				return fieldError.getField() + ": " + fieldError.getDefaultMessage();
			}
			return error.getDefaultMessage();
		}).toList();
		return ResponseEntity.status(httpStatus)
				.body(new ErrorResponse(
						httpStatus,
						request.getRequestURL().toString(),
						request.getMethod(),
						"La solicitud contiene parámetros inválidos o incompletos."
								.concat("Por favor, verifique y proporcione la información requerida antes de volver a intentarlo."),
						details.toString(),
						timestamp,
						details));
	}

	@Override
	public boolean canHandle(Exception exception) {
		return exception instanceof MethodArgumentNotValidException;
	}
}
