package com.saltaTech.category.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequest (
		@NotBlank(message = "{generic.notBlank}")
		@Size(min = 2 , max =  30, message = "{generic.size}")
		String name
) implements java.io.Serializable  {
}
