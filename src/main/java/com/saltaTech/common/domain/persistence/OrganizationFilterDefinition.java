package com.saltaTech.common.domain.persistence;

import com.saltaTech.organization.domain.persistence.Organization;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(
		name = Filters.ORGANIZATION_FILTER,
		parameters = @ParamDef(name = Filters.ORGANIZATION_SLUG_PARAM, type = String.class)
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class OrganizationFilterDefinition extends Auditable{
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;
}
