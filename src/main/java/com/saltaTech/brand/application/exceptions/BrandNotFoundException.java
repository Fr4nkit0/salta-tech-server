package com.saltaTech.brand.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.ResourceNotFoundException;

import java.io.Serializable;

public class BrandNotFoundException extends ResourceNotFoundException {
    public BrandNotFoundException(Serializable brandId) {
        super(brandId);
    }

    public BrandNotFoundException(Serializable resourceId, Throwable cause) {
        super(resourceId, cause);
    }

    @Override
    public String getResourceName() {
        return "Marca";
    }
}
