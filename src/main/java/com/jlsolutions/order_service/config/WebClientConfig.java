package com.jlsolutions.order_service.config;

import com.jlsolutions.order_service.exception.InventoryClientException;
import com.jlsolutions.order_service.exception.InventoryServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
	@Value("${inventory.service.url:http://localhost:8082}")
	private String inventoryServiceUrl;

	@Bean
	public WebClient inventoryWebClient() {
		return WebClient.builder()
				.baseUrl(inventoryServiceUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.filter(errorHandler())
				.build();
	}

	private ExchangeFilterFunction errorHandler() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			if (clientResponse.statusCode().isError()) {
				return clientResponse.bodyToMono(String.class)
						.flatMap(errorBody -> {
							if (clientResponse.statusCode().is4xxClientError()) {
								return Mono.error(new InventoryClientException(
										"Error en la petición al servicio de inventario: " + errorBody));
							} else {
								return Mono.error(new InventoryServiceException(
										"Error en el servicio de inventario: " + errorBody));
							}
						});
			}
			return Mono.just(clientResponse);
		});
	}
}
