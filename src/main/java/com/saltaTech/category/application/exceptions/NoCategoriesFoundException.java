package com.saltaTech.category.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoCategoriesFoundException extends NoDataFoundException {
	public NoCategoriesFoundException() {
	}

	public NoCategoriesFoundException(Object criteria) {
		super(criteria);
	}

	public NoCategoriesFoundException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Categorias";
	}
}
