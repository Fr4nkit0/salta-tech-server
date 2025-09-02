package com.saltaTech.organization.application.mapper;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
import com.saltaTech.auth.domain.persistence.Role;
import com.saltaTech.branch.application.mapper.BranchMapper;
import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.organization.domain.dto.request.OrganizationCreateRequest;
import com.saltaTech.organization.domain.dto.response.OrganizationResponse;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationMapper {
	private final BranchMapper branchMapper;

	public OrganizationMapper(BranchMapper branchMapper) {
		this.branchMapper = branchMapper;
	}

	public Organization toOrganization (OrganizationCreateRequest createRequest){
		return Organization.builder()
				.name(createRequest.name())
				.build();
	}
	public OrganizationResponse toOrganizationResponse (Organization organization){
		if (organization==null) return null;
		return new OrganizationResponse(
				organization.getId(),
				organization.getName(),
				organization.getSlug()
		);
	}
	public OrganizationMember toOrganizationMember (Organization organization, User user, Role role , List<Branch> branches){
		var member = OrganizationMember.builder()
				.user(user)
				.organization(organization)
				.role(role)
				.build();
		var branchesAccess = branches.stream()
				.map(branch -> branchMapper.toBranchAccess(branch, member))
				.toList();
		if (branchesAccess.isEmpty()) throw  new IllegalArgumentException("Se debe proporcionar al menos un acceso a una sucursal");
		member.getBranchAccesses().addAll(branchesAccess);
		return member;
	}
}
