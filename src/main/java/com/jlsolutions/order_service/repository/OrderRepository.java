package com.jlsolutions.order_service.repository;

import com.jlsolutions.order_service.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}