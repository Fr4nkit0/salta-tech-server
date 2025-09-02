package com.saltaTech.payment.domain.dto.request;

import java.math.BigDecimal;

public record PaymentSearchCriteria(
		BigDecimal amount,
		String paymentType
) {

}
