package com.saltaTech.sale.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoSalesFoundException extends NoDataFoundException {
	public NoSalesFoundException() {
	}

	public NoSalesFoundException(Object criteria) {
		super(criteria);
	}

	public NoSalesFoundException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Ventas";
	}
}
