package com.saltaTech.branch.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public  class BranchNotFoundException extends ResourceNotFoundException {

	public BranchNotFoundException(Serializable resourceId) {
		super(resourceId);
	}

	public BranchNotFoundException(Serializable resourceId, Throwable cause) {
		super(resourceId, cause);
	}

	@Override
	public String getResourceName() {
		return "Sucursale";
	}
}
