package com.jlsolutions.commons;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEvent {
	private String orderId;
	private OrderEventType orderEventType;  // Por ejemplo: "ORDER_CREATED"
	private List<OrderItemEvent> items;
	private LocalDateTime timestamp;
}