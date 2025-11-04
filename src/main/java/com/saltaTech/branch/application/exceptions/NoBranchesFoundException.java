package com.saltaTech.branch.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoBranchesFoundException extends NoDataFoundException {
	public NoBranchesFoundException() {
	}

	public NoBranchesFoundException(Object criteria) {
		super(criteria);
	}

	public NoBranchesFoundException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Organizaciones";
	}

}
