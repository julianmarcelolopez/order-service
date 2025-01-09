package com.jlsolutions.order_service.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryResponse {
	private String productId;
	private Integer quantity;
	private InventoryStatus status;
}

