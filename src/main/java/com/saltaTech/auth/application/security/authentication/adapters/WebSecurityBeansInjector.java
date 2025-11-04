package com.saltaTech.auth.application.security.authentication.adapters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de beans relacionados con la seguridad web.
 * <p>
 * Provee beans para la autenticación y codificación de contraseñas que serán
 * usados por Spring Security para gestionar la autenticación de usuarios.
 * </p>
 *
 * Incluye:
 * <ul>
 *   <li>AuthenticationManager para controlar el proceso de autenticación.</li>
 *   <li>PasswordEncoder para el cifrado seguro de contraseñas.</li>
 *   <li>DaoAuthenticationProvider que utiliza UserDetailsService y PasswordEncoder para autenticar usuarios.</li>
 * </ul>
 *
 * @author Franco
 */
@Configuration
public class WebSecurityBeansInjector {


	/**
	 * Provee el AuthenticationManager utilizado para manejar el proceso de autenticación.
	 * @param authenticationConfiguration configuración estándar de autenticación de Spring.
	 * @return el AuthenticationManager configurado.
	 * @throws Exception si hay problemas al obtener el manager.
	 */
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	/**
	 * Provee el codificador de contraseñas para seguridad.
	 * En este caso, se usa BCryptPasswordEncoder que es recomendado por su fortaleza y adaptabilidad.
	 * @return una instancia de PasswordEncoder.
	 */
	@Bean
	public PasswordEncoder passwordEncoder (){
		return new BCryptPasswordEncoder();
	}

	/**
	 * Crea y configura el {@link AuthenticationProvider} que se utilizará para autenticar
	 * a los usuarios basándose en una base de datos. Utiliza un {@link DaoAuthenticationProvider}
	 * que se conecta a un {@link UserDetailsService} para cargar los detalles del usuario
	 * y un {@link PasswordEncoder} para verificar las contraseñas.
	 * @return Una instancia del {@link DaoAuthenticationProvider}.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider (UserDetailsService userDetailsService){
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
}
