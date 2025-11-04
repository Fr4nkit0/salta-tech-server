package com.saltaTech.auth.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record CurrentUserResponse(
		Long id,
		@JsonProperty("first_name")
		String firstName,
		@JsonProperty("last_name")
		String lastName,
		String email,
		@JsonProperty("phone_number")
		String phoneNumber,
		@JsonProperty("is_super_user")
		boolean isSuperUser,
		@JsonProperty("branch")
		String branch
) implements Serializable {
} 