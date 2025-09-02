package com.saltaTech.payment.domain.dto.request;

import com.saltaTech.payment.domain.util.PaymentType;

import java.io.Serializable;

public record PaymentMethodUpdateRequest(
		String name,
		PaymentType type
) implements Serializable {
}
