package com.saltaTech.product.domain.dto.request;

import java.math.BigDecimal;

public record ProductSearchCriteria (
		String name,
		BigDecimal price,
		String category,
		String brand
) {
}
