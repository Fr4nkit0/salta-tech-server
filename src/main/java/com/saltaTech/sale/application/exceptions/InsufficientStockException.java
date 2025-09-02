package com.saltaTech.sale.application.exceptions;

public class InsufficientStockException extends IllegalArgumentException{
	public InsufficientStockException() {
	}

	public InsufficientStockException(Throwable cause) {
		super(cause);
	}

	public InsufficientStockException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsufficientStockException(String s) {
		super(s);
	}
}
