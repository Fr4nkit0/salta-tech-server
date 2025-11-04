package com.saltaTech.auth.application.mapper;

import com.saltaTech.auth.domain.dto.response.CurrentUserResponse;
import com.saltaTech.auth.domain.dto.response.ProfileResponse;
import com.saltaTech.auth.domain.persistence.BranchMember;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service
public class ProfileMapper {

	public CurrentUserResponse toCurrentUserResponse() {
		Object principal = getAuthenticatedPrincipal();

		return switch (principal) {
			case User user -> new CurrentUserResponse(
					user.getId(),
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getPhoneNumber(),
					user.isSuperUser(),
					null
			);
			case BranchMember member -> {
				var user = member.getUser();
				yield new CurrentUserResponse(
						user.getId(),
						user.getFirstName(),
						user.getLastName(),
						user.getEmail(),
						user.getPhoneNumber(),
						user.isSuperUser(),
						member.getBranch().getName()
				);
			}
			default -> throw new IllegalStateException("Tipo de usuario no soportado");
		};
	}

	public ProfileResponse toProfileResponse() {
		Object principal = getAuthenticatedPrincipal();

		return switch (principal) {
			case User user -> new ProfileResponse(
					new ProfileResponse.UserInfo(
							user.getId(),
							user.getFirstName(),
							user.getLastName(),
							user.getEmail(),
							user.getPhoneNumber(),
							user.isSuperUser()
					),
					null,
					null
			);
			case BranchMember member -> {
				var user = member.getUser();
				var branch = member.getBranch();
				var role = member.getRole();

				var permissions = role.getGrantedPermissions().stream()
						.map(gp -> gp.getOperation().getName())
						.toList();

				yield new ProfileResponse(
						new ProfileResponse.UserInfo(
								user.getId(),
								user.getFirstName(),
								user.getLastName(),
								user.getEmail(),
								user.getPhoneNumber(),
								user.isSuperUser()
						),
						new ProfileResponse.BranchInfo(
								branch.getId(),
								branch.getName(),
								branch.getIdentifier()
						),
						new ProfileResponse.RoleInfo(
								role.getId(),
								role.getName(),
								permissions
						)
				);
			}
			default -> throw new IllegalStateException("Tipo de usuario no soportado");
		};
	}

	private Object getAuthenticatedPrincipal() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new IllegalStateException("Usuario no autenticado");
		}
		return auth.getPrincipal();
	}
}

