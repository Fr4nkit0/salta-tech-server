package com.saltaTech.auth.application.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		var apiResponse = new ErrorResponse(
				HttpStatus.UNAUTHORIZED.value(),
				request.getRequestURL().toString(),
				request.getMethod(),
				"No se encontraron credenciales de authenticacion. Por favor, Inicie sesion para acceder" +
						"a esta funcion.",
				authException.getLocalizedMessage(),
				LocalDateTime.now(),
				null) ;
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String apiResponseAsJson = objectMapper.writeValueAsString(apiResponse);
		response.getWriter().write(apiResponseAsJson);
	}
}
