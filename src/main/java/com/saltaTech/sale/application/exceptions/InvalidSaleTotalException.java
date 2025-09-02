package com.saltaTech.sale.application.exceptions;

public class InvalidSaleTotalException extends IllegalArgumentException{
	public InvalidSaleTotalException() {
	}

	public InvalidSaleTotalException(Throwable cause) {
		super(cause);
	}

	public InvalidSaleTotalException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSaleTotalException(String s) {
		super(s);
	}
}
