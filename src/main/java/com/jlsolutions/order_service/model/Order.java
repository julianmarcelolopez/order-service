package com.jlsolutions.order_service.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "orders")
public class Order {
	@Id
	private ObjectId id;
	private String customerName;
	private LocalDateTime orderDate;
	private List<OrderItem> items;
	private double totalAmount;
	private String status;
}