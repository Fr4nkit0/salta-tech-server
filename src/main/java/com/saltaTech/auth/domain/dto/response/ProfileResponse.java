package com.saltaTech.auth.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record ProfileResponse(
		UserInfo user,
		OrganizationInfo organization,
		RoleInfo role,
		@JsonProperty("branches_access")
		List<BranchAccessInfo> branchAccess
) implements Serializable {

	public record UserInfo(
			Long id,
			@JsonProperty("first_name")
			String firstName,
			@JsonProperty("last_name")
			String lastName,
			String email,
			@JsonProperty("phone_number")
			String phoneNumber,
			@JsonProperty("is_super_user")
			boolean isSuperUser
	) implements Serializable {}

	public record OrganizationInfo(
			Long id,
			String name,
			String slug
	) implements Serializable {}

	public record RoleInfo(
			Long id,
			String name,
			List<String> permissions
	) implements Serializable {}

	public record BranchAccessInfo(
			Long id,
			String name
	) implements Serializable {}
} 