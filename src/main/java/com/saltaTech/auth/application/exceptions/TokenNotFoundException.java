package com.saltaTech.auth.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class TokenNotFoundException extends ResourceNotFoundException {
	public TokenNotFoundException (Serializable jwt){
		super(jwt);
	}

	@Override
	public String getResourceName() {
		return "JWT";
	}
}
