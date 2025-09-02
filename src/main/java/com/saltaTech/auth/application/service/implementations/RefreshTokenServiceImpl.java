package com.saltaTech.auth.application.service.implementations;

import com.saltaTech.auth.application.exceptions.TokenNotFoundException;
import com.saltaTech.auth.application.service.interfaces.RefreshTokenService;
import com.saltaTech.auth.domain.persistence.RefreshToken;
import com.saltaTech.auth.domain.repository.TokenRepository;
import com.saltaTech.user.domain.persistence.User;
import com.saltaTech.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;

	public RefreshTokenServiceImpl(TokenRepository tokenRepository, UserRepository userRepository) {
		this.tokenRepository = tokenRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void saveToken(String jwt, UserDetails userDetails , Date expiration) {
		User user = userRepository.findActiveByEmail(userDetails.getUsername())
						.orElseThrow(() -> new TokenNotFoundException("User not found"));
		tokenRepository.save(
				RefreshToken.builder()
						.token(jwt)
						.user(user)
						.expiration(expiration)
						.isValid(true)
						.build()
		);
	}

	@Override
	public void invalidateToken(String jwt) {
		Optional<RefreshToken> token= tokenRepository.findByToken(jwt);
		if (token.isPresent()){
			RefreshToken jwtToken = token.get();
			if (jwtToken.isValid()){
				jwtToken.setValid(false);
				tokenRepository.save(jwtToken);
			}
		}
	}

	@Override
	public void deleteAllTokens(String email) {
		this.tokenRepository.deleteAllByEmail(email);
	}

	@Override
	public boolean validateToken(String jwt) {
		log.debug("Se esta validando el Token");
		boolean isValid = tokenRepository.findByToken(jwt)
				.map(refreshToken -> refreshToken.isValid()&&
						refreshToken.getExpiration().after(new Date(System.currentTimeMillis())))
				.orElse(false);
		if (!isValid){
			log.debug("Token no valido");
			updateTokenStatus(jwt);
		}
		return isValid;
	}
	private void updateTokenStatus(String jwt) {
		RefreshToken refreshToken = tokenRepository.findByToken(jwt)
				.orElseThrow(()-> new TokenNotFoundException("Token not Found"));
		refreshToken.setValid(false);
		tokenRepository.save(refreshToken);
	}
}
