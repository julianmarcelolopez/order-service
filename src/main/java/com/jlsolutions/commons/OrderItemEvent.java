package com.jlsolutions.commons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemEvent {
	private String productId;
	private Integer quantity;
}