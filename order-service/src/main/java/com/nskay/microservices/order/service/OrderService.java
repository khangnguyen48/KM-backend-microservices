package com.nskay.microservices.order.service;

import com.nskay.microservices.order.client.InventoryClient;
import com.nskay.microservices.order.dto.OrderRequest;
import com.nskay.microservices.order.event.OrderPlacedEvent;
import com.nskay.microservices.order.model.Order;
import com.nskay.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

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

            //send the message to Kafka topic
            OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder()
                    .orderNumber(order.getOrderNumber())
                    .email(orderRequest.getUserDetails().email())
                    .build();
            log.info("Start - Sending orderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End - Sending orderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);


        }
        else {
            throw new RuntimeException("Product with skucode " + orderRequest.getSkuCode() + " is out of stock");
        }

    }
}
