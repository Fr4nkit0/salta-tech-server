package com.saltaTech.auth.application.service.implementations;

import com.saltaTech.auth.application.exceptions.TokenNotFoundException;
import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.auth.application.service.interfaces.AuthenticationService;
import com.saltaTech.auth.application.service.interfaces.JwtService;
import com.saltaTech.auth.application.service.interfaces.RefreshTokenService;
import com.saltaTech.auth.domain.dto.request.AuthenticationRequest;
import com.saltaTech.auth.domain.dto.response.AuthenticationResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final UserDetailsService userDetailsService;


	public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
									 JwtService jwtService, RefreshTokenService refreshTokenService,
									 UserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		String refreshToken = jwtService.extractRefreshTokenFromCookie(request);
		if (!StringUtils.hasText(refreshToken)) return;
		removeRefreshTokenCookie(response);
		refreshTokenService.invalidateToken(refreshToken);
	}

	@Override
	public AuthenticationResponse login(AuthenticationRequest authenticationRequest,
										HttpServletResponse response) {
		log.debug("Entraste al Login");
		final String email = authenticationRequest.email();
		final String password = authenticationRequest.password();
		final String tenant = OrganizationContext.getOrganizationTenant();

		String principal = email + "|" + tenant ;
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(principal, password);
		final Authentication userAuthentication = this.authenticationManager.authenticate(authentication);
		final UserDetails userDetails = (UserDetails) userAuthentication.getPrincipal();
		final Map<String, Object> extraClaims = this.generateExtraClaims(userDetails);
		final var accessToken = jwtService.generateAccessToken(userDetails,extraClaims);
		final var refreshToken = jwtService.generateRefreshToken(userDetails,extraClaims);
		final Date refreshTokenExpiration = jwtService.extractExpiration(refreshToken);
		refreshTokenService.saveToken(refreshToken,userDetails,refreshTokenExpiration) ;
		addRefreshTokenCookie(response,refreshToken,refreshTokenExpiration);
		return new AuthenticationResponse(accessToken);
	}

	@Override
	public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtService.extractRefreshTokenFromCookie(request);
		if (!StringUtils.hasText(refreshToken)) {
			throw new TokenNotFoundException("Refresh token not found");
		}
		if (!refreshTokenService.validateToken(refreshToken)) {
			throw new TokenNotFoundException("Invalid refresh token");
		}
		String email = jwtService.extractEmail(refreshToken);
		String organizationSlug = jwtService.extractOrganizationSlug(refreshToken);
		String username = email+"|"+organizationSlug;
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		Map<String, Object> extraClaims = this.generateExtraClaims(userDetails);
		String newAccessToken = jwtService.generateAccessToken(userDetails, extraClaims);
		String newRefreshToken = jwtService.generateRefreshToken(userDetails, extraClaims);
		Date newRefreshTokenExpiration = jwtService.extractExpiration(newRefreshToken);
		refreshTokenService.deleteAllTokens(email);
		refreshTokenService.saveToken(newRefreshToken, userDetails, newRefreshTokenExpiration);
		addRefreshTokenCookie(response, newRefreshToken, newRefreshTokenExpiration);
		return new AuthenticationResponse(newAccessToken);
	}

	private Map<String, Object> generateExtraClaims (UserDetails userDetails){
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("name",userDetails.getUsername()) ;
		extraClaims.put("authorities",userDetails.getAuthorities()) ;
		extraClaims.put("X-Tenant", OrganizationContext.getOrganizationTenant());
		return extraClaims;
	}
	private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, Date expiration) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge((int) (expiration.getTime() - System.currentTimeMillis()) / 1000);
		response.addCookie(cookie);
	}
	private void removeRefreshTokenCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0); // Esto elimina la cookie inmediatamente
		response.addCookie(cookie);
	}
}
