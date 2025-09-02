package com.saltaTech.product.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoProductsFoundException extends NoDataFoundException {
	public NoProductsFoundException() {
	}

	public NoProductsFoundException(Object criteria) {
		super(criteria);
	}

	public NoProductsFoundException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Productos";
	}
}
