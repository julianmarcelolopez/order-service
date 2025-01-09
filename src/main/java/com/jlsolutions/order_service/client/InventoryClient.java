package com.jlsolutions.order_service.client;

import com.jlsolutions.order_service.client.dto.InventoryResponse;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryClient {
	private final WebClient inventoryWebClient;

	public InventoryResponse checkInventory(String productId) {
		log.info("Consultando inventario para producto: {}", productId);
		return inventoryWebClient.get()
				.uri("/inventory/{productId}", productId)
				.retrieve()
				.bodyToMono(InventoryResponse.class)
				.doOnError(error -> log.error("Error al consultar inventario: {}", error.getMessage()))
				.block(Duration.ofSeconds(5)); // timeout de 5 segundos
	}

	public List<InventoryResponse> checkMultipleInventories(List<String> productIds) {
		log.info("Consultando inventario para {} productos", productIds.size());
		return Flux.fromIterable(productIds)
				.flatMap(productId ->
						inventoryWebClient.get()
								.uri("/inventory/{productId}", productId)
								.retrieve()
								.bodyToMono(InventoryResponse.class)
								.doOnError(error -> log.error("Error al consultar inventario para producto {}: {}",
										productId, error.getMessage()))
				)
				.collectList()
				.block(Duration.ofSeconds(10));
	}
}