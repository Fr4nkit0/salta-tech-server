package com.saltaTech.branch.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoBranchesException extends NoDataFoundException {

	public NoBranchesException() {
	}

	public NoBranchesException(Object criteria) {
		super(criteria);
	}

	public NoBranchesException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Sucursales";
	}
}
