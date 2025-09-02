package com.saltaTech.auth.domain.dto.response;

import java.io.Serializable;

public record LogoutResponse(
		String message
) implements Serializable {
}
