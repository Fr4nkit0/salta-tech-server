package com.saltaTech.customer.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoCustomersException extends NoDataFoundException {

	public NoCustomersException() {
		super();
	}

	public NoCustomersException(Object criteria) {
		super(criteria);
	}

	public NoCustomersException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Clientes";
	}
}
