package com.saltaTech.common.domain.persistence;

import com.saltaTech.branch.domain.persistence.Branch;
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
		name = Filters.BRANCH_FILTER,
		parameters = @ParamDef(name = Filters.BRANCH_SLUG_PARAM, type = String.class)
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class BranchFilterDefinition extends Auditable{
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;
}
