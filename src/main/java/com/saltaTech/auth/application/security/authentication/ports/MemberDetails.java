package com.saltaTech.auth.application.security.authentication.ports;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interfaz que representa los detalles de un miembro autenticable dentro del sistema.
 * <p>
 * Extiende la interfaz {@link UserDetails}
 * de Spring Security para agregar semántica específica de la aplicación.
 * <p>
 * Esta interfaz permite distinguir entre un usuario genérico y un "miembro" que puede tener
 * relaciones adicionales, como pertenencia a una sucursal con un rol específico.
 * <p>
 * @author Franco
 */
public interface MemberDetails extends UserDetails {
}
