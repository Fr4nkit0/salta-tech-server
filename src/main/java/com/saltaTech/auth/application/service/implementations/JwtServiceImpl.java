package com.saltaTech.auth.application.service.implementations;

import com.saltaTech.auth.application.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public  class JwtServiceImpl implements JwtService{
	@Value("${security.jwt.access-token-expiration-minutes}")
	private Long ACCESS_TOKEN_EXPIRATION_MINUTES;
	@Value("${security.jwt.refresh-token-expiration-days}")
	private Long REFRESH_TOKEN_EXPIRATION_DAYS;
	@Value("${security.jwt.secret-key}")
	private String SECRET_KEY ;

	private String generateToken(UserDetails userDetails, Map<String, Object> extraClaims,
								 long expirationMillis) {
		Date issuedAt = new Date(System.currentTimeMillis());
		Date expiration = new Date(issuedAt.getTime() + expirationMillis);
		return Jwts.builder()
				.header()
				.type("JWT")
				.and()
				.subject(userDetails.getUsername())
				.issuedAt(issuedAt)
				.expiration(expiration)
				.claims(extraClaims)
				.signWith(generateKey(),Jwts.SIG.HS256)
				.compact();
	}

	@Override
	public String generateAccessToken(UserDetails userDetails, Map<String, Object> extraClaims) {
		long expirationMillis = ACCESS_TOKEN_EXPIRATION_MINUTES * 60 * 1000 ;
		return generateToken(userDetails,extraClaims,expirationMillis);
	}

	@Override
	public String generateRefreshToken(UserDetails userDetails, Map<String, Object> extraClaims) {
		long expirationMillis = REFRESH_TOKEN_EXPIRATION_DAYS * 24 * 60 * 60 * 1000;
		return generateToken(userDetails,extraClaims,expirationMillis);
	}

	@Override
	public String extractEmail(String jwt) {
		return extractAllClaims(jwt).getSubject();
	}
	@Override
	public String extractJwtFromRequest(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (!StringUtils.hasText(authorizationHeader)|| !authorizationHeader.startsWith("Bearer ")){
			return null;
		}
		return  authorizationHeader.split(" ")[1];
	}

	@Override
	public String extractRefreshTokenFromCookie(HttpServletRequest request) {
		if (request.getCookies() == null) {
			return null;
		}

		for (Cookie cookie : request.getCookies()) {
			if ("refreshToken".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@Override
	public Date extractExpiration(String jwt) {
		return extractAllClaims(jwt).getExpiration();
	}

	@Override
	public String extractOrganizationSlug(String token) {
		Claims claims = extractAllClaims(token);
		return claims.get("X-Tenant", String.class);
	}
	private SecretKey generateKey (){
		byte[] key= Decoders.BASE64.decode(this.SECRET_KEY);
		return Keys.hmacShaKeyFor(key);
	}
	private Claims extractAllClaims(String jwt) {
		return Jwts.parser().verifyWith(generateKey()).build()
				.parseSignedClaims(jwt).getPayload();
	}
	@Override
	public boolean validateToken(String jwt) {
		try {
			// Verifica firma y expiración en una sola operación
			Jwts.parser()
					.verifyWith(generateKey())
					.build()
					.parseSignedClaims(jwt);
			return true;
		} catch (ExpiredJwtException ex) {
			log.debug("Token expirado: {}", ex.getMessage());
		} catch (SignatureException ex) {
			log.debug("Firma inválida: {}", ex.getMessage());
		} catch (MalformedJwtException ex) {
			log.debug("Token malformado: {}", ex.getMessage());
		} catch (UnsupportedJwtException ex) {
			log.debug("Token no soportado: {}", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			log.debug("Token vacío o inválido: {}", ex.getMessage());
		} catch (WeakKeyException ex) {
			log.error("Clave de firma débil: {}", ex.getMessage());
		} catch (Exception ex) {
			log.debug("Error validando token: {}", ex.getMessage());
		}
		return false;
	}

}





