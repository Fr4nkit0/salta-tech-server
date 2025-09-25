package com.saltaTech.auth.application.security.filter;

import com.saltaTech.auth.application.exceptions.TokenNotFoundException;
import com.saltaTech.auth.application.security.authentication.adapters.CustomUserDetailsService;
import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.auth.application.service.interfaces.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro servlet que valida y auténtica a los usuarios mediante un token JWT
 * en cada solicitud HTTP.
 * <p>
 * Este filtro extrae el token JWT de la solicitud, válida su firma, fecha de expiración
 * y consistencia con la organización actual definida en el contexto. Luego,
 * auténtica al usuario y establece su información en el {@link SecurityContextHolder}
 * para permitir el acceso seguro a los recursos protegidos.
 * <p>
 * Este filtro se ejecuta una sola vez por solicitud, gracias a {@link OncePerRequestFilter}.
 *
 * @author Franco
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final CustomUserDetailsService customUserDetailsService;
	/**
	 * Constructor para inyectar las dependencias necesarias para la autenticación JWT.
	 *
	 * @param jwtService servicio validación de tokens JWT.
	 * @param customUserDetailsService repositorio para consultar miembros de la organización.
	 */
	public JwtAuthenticationFilter(CustomUserDetailsService customUserDetailsService, JwtService jwtService) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtService = jwtService;
	}

	/**
	 * Filtra cada solicitud HTTP para validar el token JWT y autenticar al usuario.
	 * <p>
	 * Si el token JWT está presente y es válido, se verifica que pertenezca a la organización
	 * actual y se carga la información del miembro desde la base de datos. Luego, se establece
	 * la autenticación en el contexto de seguridad de Spring.
	 * <p>
	 * Si el token no existe o es inválido, se continúa la cadena de filtros sin autenticación.
	 *
	 * @param request la solicitud HTTP entrante.
	 * @param response la respuesta HTTP saliente.
	 * @param filterChain la cadena de filtros que continúa el procesamiento de la solicitud.
	 * @throws ServletException si ocurre un error durante el proceso de filtrado.
	 * @throws IOException sí ocurre un error de entrada/salida.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		// Intenta extraer el token JWT de la petición.
		log.debug("JwtAuthenticationFilter - inicio del filtro");
		final var accessToken = jwtService.extractJwtFromRequest(request);
		// Si no se encuentra un token JWT, permite que la petición continúe sin autenticación.
		if (!StringUtils.hasText(accessToken)){
			logger.debug("No hay JWT, continúo la cadena de filtros sin autenticación");
			filterChain.doFilter(request,response);
			return;
		}
		// Válida la firma y la fecha de expiración del token JWT.
		if(!jwtService.validateToken(accessToken)){
			log.debug("JWT inválido, continúo la cadena de filtros sin autenticación");
			filterChain.doFilter(request,response);
			return;
		}
		// Extrae el email del usuario (subject) del token JWT.
		final var email = jwtService.extractEmail(accessToken);
		final var tenantToken = jwtService.extractOrganizationSlug(accessToken);
		final var tenant = OrganizationContext.getOrganizationTenant();
		if(!tenantToken.equals(tenant)){
			log.error("Organization Tenant does not match the token and request");
			throw new TokenNotFoundException("The organization tenant does not match the token and header");
		}
		final var username = email + "|" + tenant ;
		final var userDetails = customUserDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities()
		);
		authentication.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
}
