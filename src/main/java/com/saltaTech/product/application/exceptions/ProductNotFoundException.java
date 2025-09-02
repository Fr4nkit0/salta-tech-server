package com.saltaTech.product.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class ProductNotFoundException extends ResourceNotFoundException {
	public ProductNotFoundException(Serializable resourceId) {
		super(resourceId);
	}

	@Override
	public String getResourceName() {
		return "Producto";
	}
}
