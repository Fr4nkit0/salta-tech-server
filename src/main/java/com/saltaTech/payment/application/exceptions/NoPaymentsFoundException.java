package com.saltaTech.payment.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoPaymentsFoundException extends NoDataFoundException {
	public NoPaymentsFoundException() {
	}

	public NoPaymentsFoundException(Object criteria) {
		super(criteria);
	}

	public NoPaymentsFoundException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Pagos";
	}
}
