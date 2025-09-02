package com.saltaTech.auth.application.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		var apiResponse = new ErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				request.getRequestURL().toString(),
				request.getMethod(),
				"Accesso denegado. No tienes los permisos necesarios para acceder a esta funcion."
				   + "Por favor, contacta al administrador si crees que esto es un error",
				accessDeniedException.getLocalizedMessage(),
				LocalDateTime.now(),
				null) ;
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String apiResponseAsJson = objectMapper.writeValueAsString(apiResponse);
		response.getWriter().write(apiResponseAsJson);

	}
}
