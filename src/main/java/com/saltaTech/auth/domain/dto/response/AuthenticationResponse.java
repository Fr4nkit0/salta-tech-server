package com.saltaTech.auth.domain.dto.response;

import java.io.Serializable;

public record AuthenticationResponse(
		String jwt
) implements Serializable {
}
