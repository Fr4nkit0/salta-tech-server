package com.saltaTech.product.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductCreateRequest(
		@Positive
		@JsonProperty("branch_id")
		Long branchID,
		@Positive
		@JsonProperty("category_id")
		Long categoryId,
		@Positive
		@JsonProperty("brand_id")
		Long brandId,
		@NotBlank
		String name,
		@NotBlank
		@Size(min = 25, max = 500)
		String description,
		@NotNull
		@DecimalMin(value = "0.01")
		BigDecimal price,
		@NotNull
		@Positive
		@Min(1)
		Integer quantity
) implements Serializable {
}
