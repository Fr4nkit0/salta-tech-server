package com.saltaTech.brand.domain.dto.response;

import java.io.Serializable;

public record BrandResponse(
		Long id,
		String name
) implements Serializable {
}
