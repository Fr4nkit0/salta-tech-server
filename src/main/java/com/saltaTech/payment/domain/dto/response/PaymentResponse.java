package com.saltaTech.payment.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public record PaymentResponse(
		Long id,
		BigDecimal amount,
		@JsonProperty("payment_method")
		String paymentMethod,
		String description
) implements Serializable {

}
