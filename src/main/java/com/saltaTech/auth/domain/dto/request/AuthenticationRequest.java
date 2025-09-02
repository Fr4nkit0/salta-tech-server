package com.saltaTech.auth.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AuthenticationRequest(
		@NotBlank(message = "{generic.notBlank}")
		@Email
		String email,
		@NotBlank(message = "{generic.notBlank}")
		String password
) implements Serializable {
}
