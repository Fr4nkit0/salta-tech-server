package com.saltaTech.branch.application.mapper;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import com.saltaTech.branch.domain.dto.response.BranchResponse;
import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.branch.domain.persistence.BranchAccess;
import com.saltaTech.organization.domain.persistence.Organization;
import org.springframework.stereotype.Service;

@Service
public class BranchMapper {


	public Branch toBranch (BranchCreateRequest createRequest, Organization organization){
		if (createRequest == null) return null;
		return Branch.builder()
				.name(createRequest.name())
				.organization(organization)
				.build();
	}
	public BranchResponse toBranchResponse (Branch branch){
		if (branch == null) return null;
		return new BranchResponse(
				branch.getId(),
				branch.getOrganization().getName(),
				branch.getName()
		);
	}
	public BranchAccess toBranchAccess (Branch branch, OrganizationMember member){
		if (branch == null) return null;
		return BranchAccess.builder()
				.organizationMember(member)
				.branch(branch)
				.build();
	}
}
