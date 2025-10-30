package com.saltaTech.payment.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

public record PaymentCreateRequest(
		@JsonProperty("sale_id")
		@Positive(message = "{generic.positive}")
		Long saleId,
		@NotNull(message = "{generic.notNull}")
		@Positive(message = "{generic.positive}")
		BigDecimal amount,
		@JsonProperty("payment_method_id")
		@NotNull(message = "{generic.notNull}")
		@Positive(message = "{generic.positive}")
		Long paymentMethodId,
		@Size(min = 2 , max =  255, message = "{generic.size}")
		String description
) implements Serializable {
}
