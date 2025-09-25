package com.saltaTech.organization.application.mapper;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
import com.saltaTech.auth.domain.persistence.Role;
import com.saltaTech.organization.domain.dto.request.OrganizationCreateRequest;
import com.saltaTech.organization.domain.dto.response.OrganizationResponse;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.stereotype.Service;
@Service
public class OrganizationMapper {


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
	public OrganizationMember toOrganizationMember (Organization organization, User user, Role role ){
		return OrganizationMember.builder()
				.user(user)
				.organization(organization)
				.role(role)
				.build();
	}
}
