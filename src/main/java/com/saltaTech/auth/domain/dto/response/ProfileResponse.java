package com.saltaTech.auth.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.branch.domain.persistence.Branch;

import java.io.Serializable;
import java.util.List;

public record ProfileResponse(
		UserInfo user,
		BranchInfo branch,
		RoleInfo role
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

	public record BranchInfo(
			Long id,
			String name,
			String indentifier
	) implements Serializable {}

	public record RoleInfo(
			Long id,
			String name,
			List<String> permissions
	) implements Serializable {}
} 