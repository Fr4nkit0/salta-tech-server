package com.saltaTech.payment.domain.dto.request;

import com.saltaTech.payment.domain.util.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record PaymentMethodCreateRequest(
		@Size(min = 2 , max =  30, message = "{generic.size}")
		String name,
		@NotNull(message = "{generic.notNull}")
		PaymentType type
) implements Serializable {
}
