package com.saltaTech.common.application.config;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
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

		if (principal instanceof OrganizationMember orgMember) {
			return Optional.of(orgMember.getUser());
		} else if (principal instanceof User user) {
			return Optional.of(user);
		}

		return Optional.empty();
	}
}
