package com.saltaTech.auth.domain.persistence;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saltaTech.organization.domain.persistence.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  Long id ;
	@Column(nullable = false)
	private String name ;
	@ManyToOne(fetch =  FetchType.EAGER,optional = false)
	@JoinColumn(name = "organization_id",nullable = false)
	private Organization organization;
	@OneToMany(
			fetch = FetchType.EAGER,
			mappedBy = "role"
	)
	@JsonIgnore
	private Set<GrantedPermission> grantedPermissions;

}
