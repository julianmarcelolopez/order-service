package com.jlsolutions.order_service.exception;

public class InventoryClientException extends RuntimeException {
	public InventoryClientException(String message) {
		super(message);
	}
}