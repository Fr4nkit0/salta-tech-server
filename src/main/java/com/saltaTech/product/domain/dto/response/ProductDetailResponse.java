package com.saltaTech.product.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.brand.domain.dto.response.BrandResponse;
import com.saltaTech.category.domain.dto.response.CategoryResponse;

import java.math.BigDecimal;

public record ProductDetailResponse(
		Long id ,
		CategoryResponse category,
		BrandResponse brand,
		String name,
		BigDecimal price,
		@JsonProperty("available_quantity")
		Integer availableQuantity
) {

}
