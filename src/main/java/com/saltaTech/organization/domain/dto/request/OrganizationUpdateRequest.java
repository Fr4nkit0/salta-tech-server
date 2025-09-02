package com.saltaTech.organization.domain.dto.request;

import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record OrganizationUpdateRequest(
		@Size(min = 2 , max =  50, message = "{generic.size}")
		String name,
		@Size(min = 2 , max =  50, message = "{generic.size}")
		String slug
)implements Serializable {
}
