package com.saltaTech.auth.application.service.interfaces;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JwtService {
	String generateAccessToken(UserDetails userDetails, Map<String ,Object> extraClaims);
	String generateRefreshToken(UserDetails userDetails, Map<String ,Object> extraClaims);
	String extractEmail(String jwt);
	String extractJwtFromRequest(HttpServletRequest request);
	String extractRefreshTokenFromCookie (HttpServletRequest request);
	Date extractExpiration(String jwt);
	String extractOrganizationSlug (String jwt);
	boolean validateToken (String jwt);
}