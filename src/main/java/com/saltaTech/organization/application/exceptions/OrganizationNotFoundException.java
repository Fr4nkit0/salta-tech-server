package com.saltaTech.organization.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class OrganizationNotFoundException extends ResourceNotFoundException {

	public OrganizationNotFoundException (Serializable organizationId){
		super(organizationId);
	}
	public OrganizationNotFoundException (Serializable organizationId,Throwable cause){
		super(organizationId,cause);
	}
	@Override
	public String getResourceName() {
		return "Organizacion";
	}

}
