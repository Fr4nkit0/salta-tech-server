package com.saltaTech.auth.application.security.authentication.adapters;

import com.saltaTech.auth.domain.repository.OrganizationMemberRepository;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import com.saltaTech.user.domain.persistence.User;
import com.saltaTech.user.domain.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
/**
 * Servicio personalizado para cargar los detalles de un usuario para autenticación.
 * <p>
 * Este servicio es responsable de obtener la información necesaria para la autenticación,
 * ya sea de un superusuario o de un miembro asociado a una organización específica.
 * <p>
 * El identificador de usuario esperado debe tener el siguiente formato:
 * <pre>
 *     email                - Para superusuarios.
 *     email|organizationSlug - Para miembros de organizaciones.
 * </pre>
 * <p>
 * La implementación verifica:
 * <ul>
 *   <li>Que el usuario exista y esté activo.</li>
 *   <li>Si el usuario es un superusuario, devuelve un UserDetails con rol SUPERUSER.</li>
 *   <li>Para usuarios normales, válida que la organización exista y que el usuario pertenezca a ella.</li>
 *   <li>Lanza excepciones específicas si alguna validación falla.</li>
 * </ul>
 *
 * @author Franco
 */
@Slf4j
@Component
public  class CustomUserDetailsService  implements UserDetailsService {
	private final UserRepository userRepository;
	private final OrganizationRepository organizationRepository;
	private final OrganizationMemberRepository organizationMemberRepository;

	public CustomUserDetailsService(OrganizationMemberRepository organizationMemberRepository,
									UserRepository userRepository,
									OrganizationRepository organizationRepository) {
		this.organizationMemberRepository = organizationMemberRepository;
		this.userRepository = userRepository;
		this.organizationRepository = organizationRepository;
	}

	/**
	 * Carga los detalles del usuario para autenticación.
	 * <p>
	 * El parámetro {@code username}  tiene el formato:
	 * <ul>
	 *     <li>email — para superusuarios.</li>
	 *     <li>email|organizationSlug — para usuarios asociados a una organización.</li>
	 * </ul>
	 * <p>
	 * El método realiza las siguientes validaciones:
	 * <ol>
	 *     <li>Busca el usuario activo por email.</li>
	 *     <li>Si el usuario es superusuario, devuelve un UserDetails con rol SUPERUSER.</li>
	 *     <li>Si no es superusuario, verifica que el formato contenga el slug de la organización.</li>
	 *     <li>Verifica que la organización exista y esté activa.</li>
	 *     <li>Verifica que el usuario pertenezca a la organización.</li>
	 * </ol>
	 *
	 * @param username es identificador del usuario que puede incluir el slug de organización.
	 * @return detalles del usuario para autenticación.
	 * @throws UsernameNotFoundException si el usuario no existe, no pertenece a la organización,
	 *                                  la organización no existe, o el formato es inválido.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String[] parts = username.split("\\|");
		String email = parts[0];
		User user = userRepository.findActiveByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No existe o está deshabilitado"));
		if (user.isSuperUser()) {
			return user;
		}
		if (parts.length != 2) {
			throw new UsernameNotFoundException("Formato inválido. Se esperaba email|organizationSlug");
		}
		String tenant = parts[1];
		boolean orgExists = organizationRepository.existsActiveByTenant(tenant);
		if (!orgExists) {
			throw new UsernameNotFoundException("Organización no encontrada o deshabilitada");
		}

		return organizationMemberRepository.findByUserEmailAndTenant(email, tenant)
				.orElseThrow(() -> new UsernameNotFoundException("No pertenece a la organización indicada"));
	}
}
