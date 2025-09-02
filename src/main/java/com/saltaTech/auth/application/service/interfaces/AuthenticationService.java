package com.saltaTech.auth.application.service.interfaces;

import com.saltaTech.auth.domain.dto.request.AuthenticationRequest;
import com.saltaTech.auth.domain.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
	void logout (HttpServletRequest request,HttpServletResponse response);
	AuthenticationResponse login (AuthenticationRequest authenticationRequest, HttpServletResponse response);
	AuthenticationResponse refreshToken (HttpServletRequest request, HttpServletResponse response);
}
