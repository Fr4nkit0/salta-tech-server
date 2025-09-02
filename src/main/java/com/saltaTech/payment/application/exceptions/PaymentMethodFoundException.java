package com.saltaTech.payment.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class PaymentMethodFoundException extends ResourceNotFoundException {
	public PaymentMethodFoundException(Serializable resourceId) {
		super(resourceId);
	}

	public PaymentMethodFoundException(Serializable resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	@Override
	public String getResourceName() {
		return "Tipo de Pago";
	}
}
