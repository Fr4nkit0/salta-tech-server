package com.saltaTech.branch.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class BranchNotFoundException extends ResourceNotFoundException {

	public BranchNotFoundException(Serializable organizationId){
		super(organizationId);
	}
	public BranchNotFoundException(Serializable organizationId, Throwable cause){
		super(organizationId,cause);
	}
	@Override
	public String getResourceName() {
		return "Organizacion";
	}

}
