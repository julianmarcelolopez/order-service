package com.jlsolutions.order_service.model.enums;

public enum OrderStatus {
	PENDING,
	CREATED,        // Orden recién creada
	PROCESSING,     // Orden en proceso de preparación
	COMPLETED,      // Orden completada
	CANCELLED       // Orden cancelada
}