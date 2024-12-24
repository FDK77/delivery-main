package com.example.delivery2.services.impl;

import com.example.delivery2.dto.REST.DelivererCoordinatesMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.delivery2.dto.REST.OrderStatusUpdateMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RabbitSendService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    static final String exchangeName = "testExchange";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public RabbitSendService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderStatusUpdate(UUID orderId, String currentStatus) {
        String formattedDate = LocalDateTime.now().format(dateTimeFormatter);
        OrderStatusUpdateMessage message = new OrderStatusUpdateMessage(orderId, currentStatus, formattedDate);
        rabbitTemplate.convertAndSend(exchangeName, "my.key", message);
    }

    public void sendDelivererCoordinatesUpdate(UUID orderId, double delivererLatitude, double delivererLongitude, double customerLatitude, double customerLongitude)
    {
        String formattedDate = LocalDateTime.now().format(dateTimeFormatter);
        DelivererCoordinatesMessage message = new DelivererCoordinatesMessage(
                orderId,
                delivererLatitude,
                delivererLongitude,
                customerLatitude,
                customerLongitude,
                formattedDate
        );
        rabbitTemplate.convertAndSend(exchangeName, "deliverer.coordinates.update", message);
    }

    public void sendOrderCreate(UUID orderId, String currentStatus) {
        sendOrderStatusUpdate(orderId, currentStatus);
    }
}

