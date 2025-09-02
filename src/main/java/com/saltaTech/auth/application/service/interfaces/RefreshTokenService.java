package com.saltaTech.auth.application.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface RefreshTokenService {
	void saveToken (String jwt , UserDetails userDetails, Date expiration) ;
	void invalidateToken(String jwt);
	void deleteAllTokens (String email);
	boolean validateToken (String jwt) ;
}
