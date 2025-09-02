package com.saltaTech.user.domain.dto.response;

import java.io.Serializable;

public record UserResponse(
		String name,
		String email,
		String phoneNumber,
		String organizationName,
		String role
) implements Serializable {}
