package com.saltaTech.user.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record RegisteredUser (
		UserInfo user,
		String organization,
		String role
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

}
