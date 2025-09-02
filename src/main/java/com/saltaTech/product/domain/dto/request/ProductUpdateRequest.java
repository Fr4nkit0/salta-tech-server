package com.saltaTech.product.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

public record ProductUpdateRequest (
		@Positive
		@JsonProperty("branch_id")
		Long branchId,
		@Positive
		@JsonProperty("category_id")
		Long categoryId,
		@Positive
		@JsonProperty("brand_id")
		Long brandId,
		@Size(min = 3,max = 50)
		String name,
		@Size(min = 25,max = 500)
		String description,
		@DecimalMin(value = "0.01", inclusive = true)
		BigDecimal price,
		@NotNull
		@Positive
		Integer quantity
) implements Serializable {
}
