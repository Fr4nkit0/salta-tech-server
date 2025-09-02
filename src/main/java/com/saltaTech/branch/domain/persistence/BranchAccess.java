package com.saltaTech.branch.domain.persistence;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
import jakarta.persistence.Entity;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "branches_access")
public class BranchAccess {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "organization_member_id")
	private OrganizationMember organizationMember;
	@ManyToOne(optional = false)
	@JoinColumn(name = "branch_id")
	private Branch branch;
}
