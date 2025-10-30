package com.saltaTech.sale.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public record SaleCreateRequest(
		@JsonProperty("customer_id")
		@NotNull(message = "{generic.notNull}")
		@Positive(message = "{generic.positive}")
		Long customerId,
		@NotNull
		@Valid
		List<SalesDetailsCreateRequest> items,
		@JsonProperty("payments")
		@Valid
		List<SalePaymentRequest> payments,
		@NotNull(message = "{generic.notNull}")
		@Positive(message = "{generic.positive}")
		BigDecimal total
) implements Serializable {

}
