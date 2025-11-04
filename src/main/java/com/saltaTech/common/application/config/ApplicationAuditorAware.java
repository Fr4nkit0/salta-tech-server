package com.saltaTech.common.application.config;

import com.saltaTech.auth.domain.persistence.BranchMember;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
public class ApplicationAuditorAware implements AuditorAware<User> {
	@Override
	public Optional<User> getCurrentAuditor() {
		final Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();

		if (authentication == null
				|| !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}

		Object principal = authentication.getPrincipal();

		return switch (principal) {
			case BranchMember orgMember -> Optional.of(orgMember.getUser());
			case User user -> Optional.of(user);
			default -> Optional.empty();
		};
	}
}
