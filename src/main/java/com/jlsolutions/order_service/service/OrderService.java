package com.jlsolutions.order_service.service;

import com.jlsolutions.commons.OrderEventType;
import com.jlsolutions.order_service.client.InventoryClient;
import com.jlsolutions.order_service.client.dto.InventoryResponse;
import com.jlsolutions.order_service.dto.OrderRequest;
import com.jlsolutions.order_service.dto.OrderItemRequest;
import com.jlsolutions.order_service.exception.InsufficientInventoryException;
import com.jlsolutions.order_service.model.Order;
import com.jlsolutions.order_service.model.OrderItem;
import com.jlsolutions.order_service.model.enums.OrderStatus;
import com.jlsolutions.commons.OrderEvent;
import com.jlsolutions.commons.OrderItemEvent;
import com.jlsolutions.order_service.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;
	private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

	public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
		this.orderRepository = orderRepository;
		this.inventoryClient = inventoryClient;
		this.kafkaTemplate = kafkaTemplate;
	}

	@Transactional
	public Order createOrder(OrderRequest request) {
		// 1. Verificar inventario
		List<InventoryResponse> inventoryResponses = inventoryClient
				.checkMultipleInventories(request.getItems().stream()
						.map(orderItemRequest -> String.valueOf(orderItemRequest.getProductId()))
						.collect(Collectors.toList()).reversed());

		// 2. Validar stock
		validateStock(request.getItems(), inventoryResponses);

		// 3. Crear y guardar la orden
		Order order = Order.builder()
				.items(mapToOrderItems(request.getItems()))
				.status(OrderStatus.CREATED)
				.createdAt(LocalDateTime.now())
				.build();

		Order savedOrder = orderRepository.save(order);

		// 4. Publicar evento
		OrderEvent orderEvent = createOrderEvent(savedOrder);
		kafkaTemplate.send("order-events", orderEvent);

		return savedOrder;
	}

	private List<OrderItem> mapToOrderItems(List<OrderItemRequest> items) {
		return items.stream()
				.map(item -> OrderItem.builder()
						.productId(new ObjectId(item.getProductId()))
						.quantity(item.getQuantity())
						.build())
				.collect(Collectors.toList());
	}

	private OrderEvent createOrderEvent(Order order) {
		return OrderEvent.builder()
				.orderId(order.getId().toString())
				.orderEventType(OrderEventType.ORDER_PLACED)
				.items(order.getItems().stream()
						.map(item -> OrderItemEvent.builder()
								.productId(item.getProductId().toString())
								.quantity(item.getQuantity())
								.build())
						.collect(Collectors.toList()))
				.timestamp(LocalDateTime.now())
				.build();
	}

	private void validateStock(List<OrderItemRequest> items,
							   List<InventoryResponse> inventoryResponses) {
		Map<String, Integer> requestedQuantities = items.stream()
				.collect(Collectors.toMap(
						item -> String.valueOf(item.getProductId()),
						OrderItemRequest::getQuantity
				));

		List<String> outOfStockProducts = inventoryResponses.stream()
				.filter(inv -> inv.getQuantity() < requestedQuantities.get(inv.getProductId()))
				.map(InventoryResponse::getProductId)
				.collect(Collectors.toList());

		if (!outOfStockProducts.isEmpty()) {
			throw new InsufficientInventoryException(
					"Productos sin stock suficiente: " + String.join(", ", outOfStockProducts));
		}
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