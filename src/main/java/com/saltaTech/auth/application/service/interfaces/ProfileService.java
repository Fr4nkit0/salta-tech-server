package com.saltaTech.auth.application.service.interfaces;

import com.saltaTech.auth.domain.dto.response.CurrentUserResponse;
import com.saltaTech.auth.domain.dto.response.ProfileResponse;

public interface ProfileService {
	/**
	 * Obtiene la información básica del usuario autenticado actualmente.
	 *
	 * @return información básica del usuario
	 */
	CurrentUserResponse getCurrentUser();

	/**
	 * Obtiene la información completa del perfil del usuario autenticado actualmente.
	 * Incluye información del usuario, organización, rol y accesos a sucursales.
	 *
	 * @return información completa del perfil del usuario
	 */
	ProfileResponse getCurrentProfile();
} 