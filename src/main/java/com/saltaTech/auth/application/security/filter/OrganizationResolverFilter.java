package com.saltaTech.auth.application.security.filter;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro Servlet que resuelve y establece el contexto de organización
 * para cada solicitud HTTP entrante, utilizando header incluidos en la URL.
 * Este filtro extrae el Header que indentifica a la organizacion de la solicitud,
 * los establece en {@link OrganizationContext} para su uso global durante el ciclo de vida
 * de la solicitud.
 * Este filtro garantiza que el contexto se limpie después de cada solicitud.
 *
 * @author Franco
 */
@Slf4j
@Component
public class OrganizationResolverFilter  extends OncePerRequestFilter {
	private static final String HEADER_TENANT = "X-Organization-Tenant";

	/**
	 * Filtra cada solicitud HTTP para:
	 * <ol>
	 *     <li>Extraer el header de organización de la ruta de la solicitud.</li>
	 *     <li>Configurar el contexto de organización para acceso global.</li>
	 * </ol>
	 *
	 * @param request      la solicitud HTTP entrante.
	 * @param response     la respuesta HTTP saliente.
	 * @param filterChain  la cadena de filtros a continuar.
	 * @throws ServletException si ocurre un error de filtrado.
	 * @throws IOException      sí ocurre un error de entrada/salida.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			String organizationTenantHeader = request.getHeader(HEADER_TENANT);
			OrganizationContext.set(organizationTenantHeader);
			filterChain.doFilter(request, response);
		} finally {
			OrganizationContext.clear();
		}
	}

}
