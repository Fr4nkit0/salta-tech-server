package com.saltaTech.payment.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoPaymentMethodFoundExeption extends NoDataFoundException {
	public NoPaymentMethodFoundExeption() {
	}

	public NoPaymentMethodFoundExeption(Object criteria) {
		super(criteria);
	}

	public NoPaymentMethodFoundExeption(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Tipos de Pagos";
	}
}
