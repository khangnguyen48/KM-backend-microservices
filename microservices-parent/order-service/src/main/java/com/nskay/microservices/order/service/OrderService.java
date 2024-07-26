package com.nskay.microservices.order.service;

import com.nskay.microservices.order.client.InventoryClient;
import com.nskay.microservices.order.dto.OrderRequest;
import com.nskay.microservices.order.model.Order;
import com.nskay.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
        boolean isInStock = inventoryClient.isInStock(orderRequest.getSkuCode(), orderRequest.getQuantity());
        if (isInStock) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderRequest.getSkuCode())
                    .price(orderRequest.getPrice())
                    .quantity(orderRequest.getQuantity())
                    .build();
            orderRepository.save(order);
        }
        else {
            throw new RuntimeException("Product with skucode " + orderRequest.getSkuCode() + " is out of stock");
        }

    }
}
