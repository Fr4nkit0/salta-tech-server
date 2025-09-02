package com.saltaTech.user.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public record UserCreateRequest(
		@NotBlank
		@Size(min = 2,max = 255,message = "{generic.size}")
		String firstName,
		@NotBlank
		@Size(min = 2,max = 255,message = "{generic.size}")
		String lastName,
		@JsonProperty("phone_number")
		String phoneNumber,
		@Email
		@NotBlank
		String email,
		@NotBlank
		@Size(min = 8,max = 16,message = "{generic.size}")
		String password,
		@NotBlank
		@Size(min = 8,max = 16,message = "{generic.size}")
		@JsonProperty(value = "password_repeated")
		String passwordRepeated,
		Long roleId,
		@NotNull
		List<Long> branchIds
) implements Serializable {
}
