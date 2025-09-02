package com.saltaTech.payment.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class PaymentNotFoundException extends ResourceNotFoundException {
	public PaymentNotFoundException(Serializable resourceId) {
		super(resourceId);
	}

	public PaymentNotFoundException(Serializable resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	@Override
	public String getResourceName() {
		return "Pago";
	}
}
