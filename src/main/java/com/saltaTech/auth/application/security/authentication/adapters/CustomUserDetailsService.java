package com.saltaTech.auth.application.security.authentication.adapters;

import com.saltaTech.auth.domain.repository.BranchMemberRepository;
import com.saltaTech.branch.domain.repository.BranchRepository;
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
	private final BranchRepository branchRepository;
	private final BranchMemberRepository branchMemberRepository;

	public CustomUserDetailsService(BranchMemberRepository branchMemberRepository,
									UserRepository userRepository,
									BranchRepository branchRepository) {
		this.branchMemberRepository = branchMemberRepository;
		this.userRepository = userRepository;
		this.branchRepository = branchRepository;
	}

	/**
	 * Carga los detalles del usuario para autenticación.
	 * <p>
	 * El parámetro {@code username}  tiene el formato:
	 * <ul>
	 *     <li>email — para superusuarios.</li>
	 *     <li>email|tenant — para usuarios asociados a una organización.</li>
	 * </ul>
	 * <p>
	 * El método realiza las siguientes validaciones:
	 * <ol>
	 *     <li>Busca el usuario activo por email.</li>
	 *     <li>Si el usuario es superusuario, devuelve un UserDetails con rol SUPERUSER.</li>
	 *     <li>Si no es superusuario, verifica que el formato contenga la indentificacion a la Sucursal.</li>
	 *     <li>Verifica que la Sucursal exista y esté activa.</li>
	 *     <li>Verifica que el usuario tenga Acceso a la Sucursal.</li>
	 * </ol>
	 *
	 * @param username es identificador del usuario que puede incluir la indentificacion de la Sucursal.
	 * @return detalles del usuario para autenticación.
	 * @throws UsernameNotFoundException si el usuario no existe, no pertenece a la organización,
	 *                                  la organización no existe, o el formato es inválido.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String[] parts = username.split("\\|");
		final var email = parts[0];
		final var user = userRepository.findActiveByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No existe o está deshabilitado"));
		if (user.isSuperUser()) {
			return user;
		}
		if (parts.length != 2) {
			throw new UsernameNotFoundException("Formato inválido. Se esperaba email|branchSlug");
		}
		final var tenant = parts[1];
		boolean branchExists = branchRepository.existsActiveByTenant(tenant);
		if (!branchExists) {
			throw new UsernameNotFoundException("Organización no encontrada o deshabilitada");
		}

		return branchMemberRepository.findByUserEmailAndTenant(email, tenant)
				.orElseThrow(() -> new UsernameNotFoundException("No pertenece a la organización indicada"));
	}
}
