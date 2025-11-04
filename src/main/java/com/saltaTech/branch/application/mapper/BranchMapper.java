package com.saltaTech.branch.application.mapper;

import com.saltaTech.auth.domain.persistence.BranchMember;
import com.saltaTech.auth.domain.persistence.Role;
import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import com.saltaTech.branch.domain.dto.response.BranchResponse;
import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.stereotype.Service;
@Service
public class BranchMapper {


	public Branch toBranch (BranchCreateRequest createRequest){
		return Branch.builder()
				.name(createRequest.name())
				.build();
	}
	public BranchResponse toBranchResponse (Branch branch){
		if (branch==null) return null;
		return new BranchResponse(
				branch.getId(),
				branch.getName(),
				branch.getIdentifier()
		);
	}
	public BranchMember toBranchMember (Branch branch, User user, Role role ){
		return BranchMember.builder()
				.user(user)
				.branch(branch)
				.role(role)
				.build();
	}
}
