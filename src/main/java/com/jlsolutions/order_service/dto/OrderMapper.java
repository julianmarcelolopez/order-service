package com.jlsolutions.order_service.dto;

import com.jlsolutions.order_service.model.Order;
import com.jlsolutions.order_service.model.OrderItem;
import com.jlsolutions.order_service.model.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

public class OrderMapper {
	private Order toOrder(OrderRequest orderRequest) {
		List<OrderItem> items = orderRequest.getItems()
				.stream()
				.map(OrderMapper::toOrderItem)
				.collect(Collectors.toList());

		double totalAmount = items.stream()
				.mapToDouble(OrderItem::getSubtotal)
				.sum();

		return Order.builder()
				.id(new ObjectId()) // Generar un nuevo ObjectId
				.customerName(orderRequest.getCustomerName())
				.createdAt(LocalDateTime.now()) // Fecha de creación actual
				.items(items)
				.totalAmount(totalAmount)
				.status(OrderStatus.PENDING) // Estado inicial (puede personalizarse)
				.build();
	}

	private static double fetchProductPrice(ObjectId productId) {
		// Aquí debes implementar la lógica para obtener el precio del producto.
		// Esto puede ser una llamada a una base de datos o un servicio externo.
		return 10.0; // Placeholder
	}

	private static String fetchProductName(ObjectId productId) {
		// Aquí debes implementar la lógica para obtener el nombre del producto.
		// Esto puede ser una llamada a una base de datos o un servicio externo.
		return "Sample Product"; // Placeholder
	}

	private static OrderItem toOrderItem(OrderItemRequest itemRequest) {
		// Mapear cada `OrderItemRequest` a `OrderItem`
		ObjectId productId = new ObjectId(itemRequest.getProductId().toString());
		double unitPrice = fetchProductPrice(productId); // Método para obtener el precio del producto

		return OrderItem.builder()
				.productId(productId)
				.productName(fetchProductName(productId)) // Método para obtener el nombre del producto
				.quantity(itemRequest.getQuantity())
				.unitPrice(unitPrice)
				.subtotal(unitPrice * itemRequest.getQuantity())
				.build();
	}


}
