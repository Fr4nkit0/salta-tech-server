package com.saltaTech.brand.application.exceptions;

import com.saltaTech.common.application.exceptions.custom.NoDataFoundException;

public class NoBrandsFoundException extends NoDataFoundException {
    public NoBrandsFoundException() {
        super();
    }

    public NoBrandsFoundException(Object criteria) {
        super(criteria);
    }

    public NoBrandsFoundException(Object criteria, Throwable cause) {
        super(criteria, cause);
    }

    @Override
    public String getDataType() {
        return "Marcas";
    }
}
