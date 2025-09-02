package com.saltaTech.category.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class CategoryNotFoundException extends ResourceNotFoundException {
	public CategoryNotFoundException(Serializable resourceId) {
		super(resourceId);
	}

	@Override
	public String getResourceName() {
		return "Categoria";
	}
}
