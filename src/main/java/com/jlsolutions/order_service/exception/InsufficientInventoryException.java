package com.jlsolutions.order_service.exception;

public class InsufficientInventoryException extends RuntimeException {
	public InsufficientInventoryException(String message) {
		super(message);
	}
}