package com.jlsolutions.order_service.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemRequest {
	private String productId;
	private Integer quantity;

}