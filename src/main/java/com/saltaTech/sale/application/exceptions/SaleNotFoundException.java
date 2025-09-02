package com.saltaTech.sale.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class SaleNotFoundException extends ResourceNotFoundException {
	public SaleNotFoundException(Serializable resourceId) {
		super(resourceId);
	}

	public SaleNotFoundException(Serializable resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	@Override
	public String getResourceName() {
		return "Venta";
	}
}
