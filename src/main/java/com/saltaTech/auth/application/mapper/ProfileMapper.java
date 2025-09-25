package com.saltaTech.auth.application.mapper;

import com.saltaTech.auth.domain.dto.response.CurrentUserResponse;
import com.saltaTech.auth.domain.dto.response.ProfileResponse;
import com.saltaTech.auth.domain.persistence.OrganizationMember;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service
public class ProfileMapper {

	public CurrentUserResponse toCurrentUserResponse (){
		Object principal = getAuthenticatedPrincipal() ;

		if (principal instanceof User user) {
			return new CurrentUserResponse(
					user.getId(),
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getPhoneNumber(),
					user.isSuperUser(),
					null // Superuser no pertenece a una organización específica
			);
		} else if (principal instanceof OrganizationMember member) {
			var user = member.getUser();
			return new CurrentUserResponse(
					user.getId(),
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getPhoneNumber(),
					user.isSuperUser(),
					member.getOrganization().getSlug()
			);
		}

		throw new IllegalStateException("Tipo de usuario no soportado");
	}

	public ProfileResponse toProfileResponse (){
		Object principal = getAuthenticatedPrincipal() ;
		if (principal instanceof User user) {
			// Para superusuarios, solo devolvemos información del usuario
			return new ProfileResponse(
					new ProfileResponse.UserInfo(
							user.getId(),
							user.getFirstName(),
							user.getLastName(),
							user.getEmail(),
							user.getPhoneNumber(),
							user.isSuperUser()
					),
					null, // Superuser no pertenece a una organización específica
					null );
		} else if (principal instanceof OrganizationMember member) {
			var user = member.getUser();
			var organization = member.getOrganization();
			var role = member.getRole();

			final var permissions = role.getGrantedPermissions()
					.stream()
					.map(grantedPermission -> grantedPermission.getOperation().getName())
					.toList();

			return new ProfileResponse(
					new ProfileResponse.UserInfo(
							user.getId(),
							user.getFirstName(),
							user.getLastName(),
							user.getEmail(),
							user.getPhoneNumber(),
							user.isSuperUser()
					),
					new ProfileResponse.OrganizationInfo(
							organization.getId(),
							organization.getName(),
							organization.getSlug()
					),
					new ProfileResponse.RoleInfo(
							role.getId(),
							role.getName(),
							permissions
					));
		}

		throw new IllegalStateException("Tipo de usuario no soportado");
	}

	private Object getAuthenticatedPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalStateException("Usuario no autenticado");
		}

		return authentication.getPrincipal();
	}

}
