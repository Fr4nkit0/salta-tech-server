package com.saltaTech.payment.domain.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

public record PaymentUpdateRequest(
		BigDecimal amount,
		String description
) implements Serializable {
}
