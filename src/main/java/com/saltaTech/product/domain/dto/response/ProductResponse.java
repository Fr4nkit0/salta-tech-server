package com.saltaTech.product.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductResponse (
		Long id ,
		String category,
		String brand,
		String name,
		BigDecimal price,
		@JsonProperty("available_quantity")
		Integer availableQuantity
)implements Serializable {
}
