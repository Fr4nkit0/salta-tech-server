package com.saltaTech.user.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record RegisteredUser (
		UserInfo user,
		String organization,
		String role,
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
			String phoneNumber
	) implements Serializable {}

	public record BranchAccessInfo(
			String name
	) implements Serializable {}
}
