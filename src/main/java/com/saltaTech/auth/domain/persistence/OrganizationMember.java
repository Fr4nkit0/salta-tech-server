package com.saltaTech.auth.domain.persistence;

import com.saltaTech.auth.application.security.authentication.ports.MemberDetails;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.user.domain.persistence.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Entidad que representa a un miembro dentro de una Organizacion.
 * Implementa la interfaz {@link MemberDetails} para usarse en el sistema de seguridad.
 *
 * Cada miembro está asociado a un {@link User}, una {@link Organization} y un {@link Role}.
 * Además, mantiene un conjunto de accesos a sucursales ({@link BranchAccess}).
 *
 * La clase provee los detalles necesarios para la autenticación y autorización,
 * incluyendo los permisos que otorga el rol dentro de la organización.
 *
 * La autoridad (roles y permisos) se construye combinando los permisos
 * explícitos otorgados al rol y un permiso basado en el nombre del rol y la organización.
 *
 * @author Franco
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "organizations_members")
public class OrganizationMember implements MemberDetails {
	@Id
	@GeneratedValue( strategy =  GenerationType.IDENTITY)
	private Long id ;
	@ManyToOne
	@JoinColumn(
			name = "user_id",
			nullable = false
	)
	private User user ;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(
			name = "organization_id",
			nullable = false
	)
	private Organization organization;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(
			name = "role_id",
			nullable = false
	)
	private  Role role ;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (role ==null) return null;
		if (role.getGrantedPermissions()==null) return null;
		List<SimpleGrantedAuthority> authorities= new java.util.ArrayList<>
				(role.getGrantedPermissions().stream()
						.map(each -> new SimpleGrantedAuthority(each.getOperation().getName()))
						.toList());
		authorities.add(new SimpleGrantedAuthority(getRoleName()));
		return authorities;
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}


	private String formatOrganizationName(String organizationName) {
		return organizationName
				.toUpperCase()           // Convertir a mayúsculas
				.replaceAll("\\s+", "_") // Reemplazar espacios por guiones bajos
				.trim();                 // Eliminar espacios al inicio y final
	}
	public String getRoleName (){
		return role.getName().toUpperCase() + "_" +
				formatOrganizationName(organization.getName());
	}
}
