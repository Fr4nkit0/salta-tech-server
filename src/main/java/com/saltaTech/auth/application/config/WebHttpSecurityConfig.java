package com.saltaTech.auth.application.config;

import com.saltaTech.auth.application.security.filter.JwtAuthenticationFilter;
import com.saltaTech.auth.application.security.filter.OrganizationResolverFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebHttpSecurityConfig {
	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final OrganizationResolverFilter organizationResolverFilter;
	private final AuthorizationManager<RequestAuthorizationContext> authorizationManager;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final AccessDeniedHandler accessDeniedHandler;

	public WebHttpSecurityConfig(AccessDeniedHandler accessDeniedHandler, AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter, OrganizationResolverFilter organizationResolverFilter, AuthorizationManager<RequestAuthorizationContext> authorizationManager, AuthenticationEntryPoint authenticationEntryPoint) {
		this.accessDeniedHandler = accessDeniedHandler;
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.organizationResolverFilter = organizationResolverFilter;
		this.authorizationManager = authorizationManager;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				// Configura CORS con la configuración por defecto.
				.cors(withDefaults())
				// Deshabilita la protección contra ataques CSRF, ya que en una API REST stateless con JWT no es necesaria.
				.csrf(AbstractHttpConfigurer::disable)
				// Configura la gestión de sesiones para que sea stateless, ya que la autenticación se basa en tokens JWT.
				.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// Asigna el proveedor de autenticación personalizado.
				.authenticationProvider(authenticationProvider)
				// Añade el filtro de resolución de organización antes del filtro de autenticación por nombre de usuario y contraseña.
				.addFilterBefore(organizationResolverFilter, UsernamePasswordAuthenticationFilter.class)
				// Añade el filtro JWT antes del filtro de autenticación por nombre de usuario y contraseña.
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				// Configura las reglas de autorización para las peticiones HTTP.
				.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
						.requestMatchers(
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/api-docs/**",
								"/swagger-resources/**",
								"/webjars/**"
						).permitAll()
						// Cualquier otra petición requiere ser evaluada por el AuthorizationManager.
						.anyRequest()
						.access(authorizationManager)
				)
				.exceptionHandling(exceptionConfig -> {
					exceptionConfig.authenticationEntryPoint(authenticationEntryPoint);
					exceptionConfig.accessDeniedHandler(accessDeniedHandler);
				})
				// Construye la cadena de filtros de seguridad.
				.build();
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource(@Value("${cors.allowed-origins}")
															   String allowedOrigins) {

		CorsConfiguration configuration = new CorsConfiguration();
		List<String> origins = Arrays.stream(allowedOrigins.split(","))
				.map(String::trim)
				.toList();
		configuration.setAllowedOrigins(origins); // origen de tu frontend
		configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
