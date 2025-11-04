package com.saltaTech.branch.domain.dto.request;

import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record BranchUpdateRequest(
		@Size(min = 2 , max =  50, message = "{generic.size}")
		String name,
		@Size(min = 2 , max =  50, message = "{generic.size}")
		String identifier
)implements Serializable {
}
