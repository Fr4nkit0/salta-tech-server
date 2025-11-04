package com.saltaTech.auth.application.security.filter;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro Servlet que resuelve y establece el contexto de sucursal
 * para cada solicitud HTTP entrante, utilizando un header.
 * Este filtro extrae el Header que indentifica a la sucursal de la solicitud,
 * los establece en {@link BranchContext} para su uso global durante el ciclo de vida
 * de la solicitud.
 * Este filtro garantiza que el contexto se limpie después de cada solicitud.
 *
 * @author Franco
 */
@Slf4j
@Component
public class BranchResolverFilter extends OncePerRequestFilter {
	private static final String HEADER_TENANT = "X-Branch-Tenant";

	/**
	 * Filtra cada solicitud HTTP para:
	 * <ol>
	 *     <li>Extraer el header  de la solicitud.</li>
	 *     <li>Configurar el contexto de la Sucursal para acceso global.</li>
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
			final var branchTenantHeader = request.getHeader(HEADER_TENANT);
			BranchContext.set(branchTenantHeader);
			filterChain.doFilter(request, response);
		} finally {
			BranchContext.clear();
		}
	}

}
