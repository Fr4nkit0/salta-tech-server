package com.saltaTech.sale.domain.dto.response;
import java.io.Serializable;
import java.math.BigDecimal;
public record ItemsResponse(
		Long productId,
		String productName,
		Integer quantity,
		BigDecimal price
) implements Serializable {
}
