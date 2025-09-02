package com.saltaTech.common.application.exceptions.handler;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;
import com.saltaTech.common.application.exceptions.strategy.ExceptionHandlerStrategy;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Component
public class NoDataFoundExceptionHandler implements ExceptionHandlerStrategy {

    @Override
    public ResponseEntity<ErrorResponse> handlerException(Exception exception, HttpServletRequest request,
														  HttpServletResponse response, LocalDateTime timestamp) {
        NoDataFoundException noDataEx = (NoDataFoundException) exception;
        int httpStatus = HttpStatus.NOT_FOUND.value();

        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(
                        httpStatus,
                        request.getRequestURL().toString(),
                        request.getMethod(),
                        "No se encontraron datos del tipo solicitado.",
                        noDataEx.getMessage(), // mensaje generado dinámicamente por la excepción
                        timestamp,
                        null));
    }

    @Override
    public boolean canHandle(Exception exception) {
        return exception instanceof NoDataFoundException;
    }
}