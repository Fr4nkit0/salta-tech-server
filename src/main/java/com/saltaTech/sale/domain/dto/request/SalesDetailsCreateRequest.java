package com.saltaTech.sale.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;

public record SalesDetailsCreateRequest(
		@NotNull(message = "{generic.notNull}")
		@Positive(message = "{generic.positive}")
		Long productId,
		@NotNull(message = "{generic.notNull}")
		@Positive(message = "{generic.positive}")
		Integer quantity,
		@NotNull(message = "{generic.notNull}")
		@Positive(message = "{generic.positive}")
		BigDecimal price
) implements Serializable {
}
