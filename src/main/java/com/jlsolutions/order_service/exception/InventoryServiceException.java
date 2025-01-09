package com.jlsolutions.order_service.exception;

public class InventoryServiceException extends RuntimeException {
	public InventoryServiceException(String message) {
		super(message);
	}
}