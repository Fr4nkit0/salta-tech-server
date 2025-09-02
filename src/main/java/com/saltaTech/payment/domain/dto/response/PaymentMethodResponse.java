package com.saltaTech.payment.domain.dto.response;

import java.io.Serializable;

public record PaymentMethodResponse(
		Long id,
		String name,
		String type
) implements Serializable {
}
