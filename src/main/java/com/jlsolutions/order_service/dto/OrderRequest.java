package com.jlsolutions.order_service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {
	private String customerName;
	private String customerEmail;
	private List<OrderItemRequest> items;
	private String shippingAddress;

}