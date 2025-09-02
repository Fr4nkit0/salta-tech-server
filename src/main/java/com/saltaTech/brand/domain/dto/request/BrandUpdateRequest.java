package com.saltaTech.brand.domain.dto.request;

import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record BrandUpdateRequest(
		@Size(min = 2 , max =  30, message = "{generic.size}")
		String name
) implements Serializable {
}
