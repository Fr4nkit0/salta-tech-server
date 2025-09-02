package com.saltaTech.organization.domain.dto.request;

import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record OrganizationCreateRequest(
		@Size(min = 2 , max =  50, message = "{generic.size}")
		@NotBlank(message = "{generic.notBlank}")
		String name,
		@Size(min = 2 , max =  50, message = "{generic.size}")
		@NotBlank(message = "{generic.notBlank}")
		String slug,
		BranchCreateRequest branch
)implements Serializable {
}
