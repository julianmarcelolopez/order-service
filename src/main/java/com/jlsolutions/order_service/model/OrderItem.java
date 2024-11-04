package com.jlsolutions.order_service.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class OrderItem {
	private ObjectId productId;    // Usando ObjectId para coincidir con el formato de Products
	private String productName;
	private int quantity;
	private double unitPrice;
	private double subtotal;
}