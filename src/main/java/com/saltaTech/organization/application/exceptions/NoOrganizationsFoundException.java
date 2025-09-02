package com.saltaTech.organization.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoOrganizationsFoundException extends NoDataFoundException {
	public NoOrganizationsFoundException() {
	}

	public NoOrganizationsFoundException(Object criteria) {
		super(criteria);
	}

	public NoOrganizationsFoundException(Object criteria, Throwable cause) {
		super(criteria, cause);
	}

	@Override
	public String getDataType() {
		return "Organizaciones";
	}

}
