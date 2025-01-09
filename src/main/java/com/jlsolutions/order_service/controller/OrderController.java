package com.jlsolutions.order_service.controller;

import com.jlsolutions.order_service.dto.OrderRequest;
import com.jlsolutions.order_service.model.Order;
import com.jlsolutions.order_service.service.OrderService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
		Order createdOrder = orderService.createOrder(orderRequest);
		return ResponseEntity.ok(createdOrder);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable String id) {
		return orderService.getOrderById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		List<Order> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}
}