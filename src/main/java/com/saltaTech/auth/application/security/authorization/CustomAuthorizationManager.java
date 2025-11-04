package com.saltaTech.auth.application.security.authorization;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import com.saltaTech.auth.domain.persistence.GrantedPermission;
import com.saltaTech.auth.domain.persistence.Operation;
import com.saltaTech.auth.domain.persistence.BranchMember;
import com.saltaTech.auth.domain.repository.OperationRepository;
import com.saltaTech.auth.domain.repository.BranchMemberRepository;
import com.saltaTech.user.domain.persistence.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Administrador de autorización personalizado que evalúa si un usuario autenticado
 * tiene permiso para acceder a una URL y método HTTP específicos.
 * <p>
 * Este componente implementa {@link AuthorizationManager} y se integra con Spring Security
 * para decidir si una solicitud HTTP debe ser permitida o denegada según las operaciones
 * públicas definidas y los permisos otorgados al usuario autenticado.
 * <p>
 * Consulta las operaciones y permisos desde la base de datos usando
 * {@link OperationRepository} y {@link BranchMemberRepository}.
 *
 * @author Franco
 */
@Slf4j
@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	private final OperationRepository operationRepository;
	/**
	 * Constructor para inyectar los repositorios necesarios para consultar
	 * operaciones.
	 *
	 * @param operationRepository repositorio para operaciones y rutas permitidas.
	 */
	public CustomAuthorizationManager(OperationRepository operationRepository) {
		this.operationRepository = operationRepository;
	}
	/**
	 * Evalúa si la solicitud HTTP actual está autorizada para el usuario autenticado.
	 * <p>
	 * Si la URL y étodo correspondemn a una operación pública, se permite el acceso.
	 * De lo contrario, verifica que el usuario autenticado posea permisos explícitos
	 * para acceder a la URL y método solicitados.
	 *
	 * @param authentication proveedor del objeto {@link Authentication} del usuario actual.
	 * @param requestContext contexto de autorización con información de la solicitud HTTP.
	 * @return una {@link AuthorizationDecision} indicando si se permite o deniega el acceso.
	 * @throws AuthenticationCredentialsNotFoundException si no hay usuario autenticado.
	 */
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication,
									   RequestAuthorizationContext requestContext) {
		HttpServletRequest request= requestContext.getRequest();
		String url = extractUrl(request);
		String method=request.getMethod();
		log.debug("Chequeando la authorization para URL: {} y HTTP metodo: {}", url, method);
		if (isPublic(url,method)){
			log.debug("URL {} con method {} es public. Concediendo el acceso.", url, method);
			return new AuthorizationDecision(true);
		}
		log.debug("Acceso por usuario en la URL: {} con method: {} ", url, method);
		return new AuthorizationDecision(isGranted(url,method,authentication.get()));
	}
	/**
	 * Extrae la URL relativa de la solicitud HTTP, eliminando el contexto de la aplicación.
	 *
	 * @param request la solicitud HTTP.
	 * @return la URL relativa a evaluar.
	 */
	private String extractUrl(HttpServletRequest request) {
		String contextPath= request.getContextPath();
		String url = request.getRequestURI();
		url = url.replace(contextPath,"");
		log.debug("URL extraida: {}", url);
		return url;
	}
	/**
	 * Determina si una operación (URL + método) es de acceso público.
	 *
	 * @param url URL relativa.
	 * @param method método HTTP.
	 * @return true si la operación es pública, false en caso contrario.
	 */
	private boolean isPublic (String url,String method){
		boolean isPublic = operationRepository.findByPublicAccess()
				.stream().anyMatch(getOperationPredicate(url, method));
		log.debug("La URL {} con method {} es public:{}", url, method,isPublic);
		return isPublic ;
	}
	/**
	 * Verifica si el usuario autenticado tiene permiso para la operación solicitada.
	 *
	 * @param url URL relativa.
	 * @param method método HTTP.
	 * @param authentication objeto de autenticación del usuario.
	 * @return true si se permite el acceso, false en caso contrario.
	 * @throws AuthenticationCredentialsNotFoundException si el usuario no está autenticado.
	 */
	private boolean isGranted (String url,String method, Authentication authentication){
		if (authentication == null) {
			log.debug("User no logeado");
			throw new AuthenticationCredentialsNotFoundException("User not logged in");
		}
		if (!(authentication instanceof UsernamePasswordAuthenticationToken authenticationToken) ) {
			log.debug("User no logeado, no es instancia de UsernamePasswordAuthenticationToken");
			throw new AuthenticationCredentialsNotFoundException("User not logged in," +
					" not instanceof UsernamePasswordAuthenticationToken");
		}
		Object principal = authenticationToken.getPrincipal();

		if (principal instanceof User user) {
			log.debug("Autenticado como superusuario: {}", user.getEmail());

			if (!user.isSuperUser()) {
				log.warn("El usuario {} no es superuser pero fue autenticado como tal", user.getEmail());
				throw new AccessDeniedException("User is not superuser");
			}

			return true; // ✅ acceso permitido para superuser
		}

		if (principal instanceof BranchMember member) {
			String email = member.getUser().getEmail();
			String tenant = BranchContext.getBranchTenant();
			log.debug("Usuario {} autenticado como miembro de la organización {}", email, tenant);

			return obtainedOperations(member).stream()
					.anyMatch(getOperationPredicate(url, method));
		}

		log.warn("Tipo de principal no soportado: {}", principal.getClass());
		throw new AuthenticationCredentialsNotFoundException("Unsupported principal type: " + principal.getClass().getName());
	}
	/**
	 * Obtiene la lista de operaciones permitidas para el usuario autenticado.
	 *
	 * @return lista de operaciones habilitadas para el usuario en la organizacion .
	 */
	private List<Operation> obtainedOperations(BranchMember member) {
		return  member.getRole().getGrantedPermissions()
				.stream()
				.map(GrantedPermission::getOperation)
				.toList();
	}
	/**
	 * Crea un predicado para verificar si una operación coincide con la URL y método HTTP.
	 *
	 * @param url URL relativa.
	 * @param method método HTTP.
	 * @return predicado que evalúa coincidencia de operación.
	 */
	private static Predicate<Operation> getOperationPredicate(String url, String method) {
		log.debug("Buscando operación para URL: [{}] y método: [{}]", url, method);

		return operation -> {
			String basePath = operation.getModule().getBasePath();
			String fullPathPattern = basePath.concat(operation.getPath());

			log.trace("Evaluando operación: [{}] [{}] (Patrón completo: [{}])",
					operation.getHttpMethod(), operation.getPath(), fullPathPattern);

			Pattern pattern = Pattern.compile(fullPathPattern);
			Matcher matcher = pattern.matcher(url);
			boolean patternMatch = matcher.matches();
			boolean methodMatch = operation.getHttpMethod().equalsIgnoreCase(method);

			if (log.isDebugEnabled()) {
				log.debug("Resultado para ['{}'] [{}]: Coincidencia patrón={}, método={}",
						operation.getName(), operation.getHttpMethod(), patternMatch, methodMatch);
			}

			if (patternMatch && methodMatch) {
				log.info("Operación encontrada: [{}] [{}] para URL: [{}]",
						operation.getHttpMethod(), fullPathPattern, url);
			}

			return patternMatch && methodMatch;
		};
	}
}
