package com.saltaTech.auth.application.controller;

import com.saltaTech.auth.application.service.interfaces.AuthenticationService;
import com.saltaTech.auth.application.service.interfaces.ProfileService;
import com.saltaTech.auth.domain.dto.request.AuthenticationRequest;
import com.saltaTech.auth.domain.dto.response.AuthenticationResponse;
import com.saltaTech.auth.domain.dto.response.LogoutResponse;
import com.saltaTech.auth.domain.dto.response.CurrentUserResponse;
import com.saltaTech.auth.domain.dto.response.ProfileResponse;
import com.saltaTech.common.domain.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "Organization")
@Tag(name = "Autenticación", description = "Contiene todas las operaciones relacionadas con la autenticación y gestión de sesión de los usuarios.")
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	private final ProfileService profileService;

	public AuthenticationController(AuthenticationService authenticationService, 
								   ProfileService profileService) {
		this.authenticationService = authenticationService;
		this.profileService = profileService;
	}
	@PostMapping("/authenticate")
	@Operation(summary = "Iniciar sesión", description = "Autentica al usuario con sus credenciales y devuelve un token JWT.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Autenticación exitosa",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = AuthenticationResponse.class))
			),
			@ApiResponse(
					responseCode = "401",
					description = "Credenciales inválidas",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<AuthenticationResponse> login (
			@RequestBody @Valid AuthenticationRequest authenticationRequest, HttpServletResponse response){
		return ResponseEntity.ok(authenticationService.login(authenticationRequest,response));
	}

	@PostMapping("/logout")
	@Operation(summary = "Cerrar sesión", description = "Finaliza la sesión actual del usuario.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Logout exitoso",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = LogoutResponse.class))
			)
	})
	public ResponseEntity<LogoutResponse> logout (HttpServletRequest request, HttpServletResponse response){
		authenticationService.logout(request,response);
		return ResponseEntity.ok(new LogoutResponse("Logout exitoso"));
	}

	@PostMapping("/refresh")
	@Operation(summary = "Refrescar token", description = "Genera un nuevo token JWT válido a partir de un refresh token.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Token renovado exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = AuthenticationResponse.class))
			),
			@ApiResponse(
					responseCode = "401",
					description = "Refresh token inválido o expirado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request,
															   HttpServletResponse response) {
		return ResponseEntity.ok(authenticationService.refreshToken(request, response));
	}

	@GetMapping("/me")
	@Operation(
			summary = "Obtener usuario actual",
			description = "Devuelve la información del usuario autenticado.",
			security = {
					@SecurityRequirement(name = "Bearer Authentication")
			}
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Usuario obtenido exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = CurrentUserResponse.class))
			),
			@ApiResponse(
					responseCode = "401",
					description = "Usuario no autenticado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<CurrentUserResponse> getCurrentUser() {
		return ResponseEntity.ok(profileService.getCurrentUser());
	}


	@GetMapping("/profile")
	@Operation(
			summary = "Obtener perfil actual",
			description = "Devuelve el perfil detallado del usuario autenticado.",
			security = {
					@SecurityRequirement(name = "Bearer Authentication")
			}
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Perfil obtenido exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ProfileResponse.class))
			),
			@ApiResponse(
					responseCode = "401",
					description = "Usuario no autenticado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<ProfileResponse> getCurrentProfile() {
		return ResponseEntity.ok(profileService.getCurrentProfile());
	}

}
