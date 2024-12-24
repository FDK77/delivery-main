package com.example.delivery2.services;

import com.example.deliveryapi.dto.GraphQL.InputOrder;
import com.example.deliveryapi.dto.GraphQL.OrderOutput;
import com.example.deliveryapi.dto.REST.CreateOrderRequest;
import com.example.deliveryapi.dto.REST.OrderResponse;
import com.example.deliveryapi.dto.REST.UpdateOrderRequest;
import com.example.delivery2.models.Order;

import java.util.List;
import java.util.UUID;

public interface IOrderService {
    List<OrderResponse> showAllOrders();

    List<OrderOutput> showAllOrdersDgs();

    List<Order> getAllOrders();

    Order getOrderById(UUID id);

    OrderResponse showOrderById(UUID id);

    OrderOutput showOrderByIdDgs(UUID id);

    OrderResponse createOrder(CreateOrderRequest order);

    OrderOutput createOrderDgs(InputOrder order);


    OrderResponse updateOrder(UUID id, UpdateOrderRequest order);

    OrderResponse updateStatus(UUID id, String status);

    OrderOutput updateOrderDgs(UUID id, InputOrder order);

    boolean deleteOrder(UUID id);
}
