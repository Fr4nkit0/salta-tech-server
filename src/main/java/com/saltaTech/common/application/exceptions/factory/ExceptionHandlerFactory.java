package com.saltaTech.common.application.exceptions.factory;

import com.saltaTech.common.application.exceptions.handler.DefaultExceptionHandler;
import com.saltaTech.common.application.exceptions.strategy.ExceptionHandlerStrategy;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExceptionHandlerFactory {
	private final List<ExceptionHandlerStrategy> exceptionHandlerStrategies;

	public ExceptionHandlerFactory(List<ExceptionHandlerStrategy> exceptionHandlerStrategies) {
		this.exceptionHandlerStrategies = exceptionHandlerStrategies;
	}

	public ResponseEntity<ErrorResponse> getHandler(Exception exception, HttpServletRequest request,
													HttpServletResponse response, LocalDateTime timestamp) {
		return exceptionHandlerStrategies.stream()
				.filter(strategy -> strategy.canHandle(exception))
				.findFirst()
				.map(strategy -> strategy.handlerException(exception, request, response, timestamp))
				.orElse(new DefaultExceptionHandler().handlerException(exception, request, response, timestamp));
	}
}
