package com.saltaTech.customer.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class CustomerNotFoundException extends ResourceNotFoundException {

	public CustomerNotFoundException(Serializable customerId) {
		super(customerId);
	}

	public CustomerNotFoundException(Serializable customerId, Throwable cause) {
		super(customerId, cause);
	}

	@Override
	public String getResourceName() {
		return "Cliente";
	}
}