package com.saltaTech.auth.domain.dto.response;

import java.io.Serializable;

public record RegisteredUser(
		String name,
		String username,
		String jwt
) implements Serializable {
}
