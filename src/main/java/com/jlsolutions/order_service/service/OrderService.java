package com.jlsolutions.order_service.service;

import com.jlsolutions.order_service.model.Order;
import com.jlsolutions.order_service.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Order createOrder(Order order) {
		return orderRepository.save(order);
	}

	public Optional<Order> getOrderById(String id) {
		return orderRepository.findById(id);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
}