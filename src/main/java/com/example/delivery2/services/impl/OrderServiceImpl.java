package com.example.delivery2.services.impl;

import com.example.deliveryapi.dto.GraphQL.InputOrder;
import com.example.deliveryapi.dto.GraphQL.OrderOutput;
import com.example.deliveryapi.dto.REST.CreateOrderRequest;
import com.example.deliveryapi.dto.REST.OrderResponse;
import com.example.deliveryapi.dto.REST.UpdateOrderRequest;
import com.example.delivery2.models.Deliverer;
import com.example.delivery2.models.Order;
import com.example.delivery2.models.Status;
import com.example.delivery2.repositories.OrderRepository;

import com.example.delivery2.services.IOrderService;
import com.example.deliveryapi.exceptions.OrderNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements IOrderService {

    private OrderRepository orderRepository;
    private RabbitSendService rabbitSendService;

    private DelivererServiceImpl delivererService;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;
    @Autowired
    public void setDelivererService(DelivererServiceImpl delivererService) {
        this.delivererService = delivererService;
    }

    @Autowired
    public void setRabbitSendService(RabbitSendService rabbitSendService) {this.rabbitSendService = rabbitSendService;}

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public OrderServiceImpl(RabbitTemplate rabbitTemplate, ModelMapper modelMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderResponse> showAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();
    }

    @Override
    public List<OrderOutput> showAllOrdersDgs() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderOutput.class))
                .toList();
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));
    }
    @Override
    public OrderResponse showOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderOutput showOrderByIdDgs(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));
        return modelMapper.map(order, OrderOutput.class);
    }


    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        List<Deliverer> deliverers = delivererService.getAllDeliverers();
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(LocalDateTime.now().plusHours(ThreadLocalRandom.current().nextInt(1, 3)));
        order.setStatus(Status.ASSEMBLING);
        order.setDeliverer(deliverers.get(ThreadLocalRandom.current().nextInt(0,deliverers.size())));
        order.setCustomerLatitude(orderRequest.getCustomerLatitude());
        order.setCustomerLongitude(orderRequest.getCustomerLongitude());
        order.setDelivererLatitude(orderRequest.getDelivererLatitude());
        order.setDelivererLongitude(orderRequest.getDelivererLongitude());
        orderRepository.save(order);
        rabbitSendService.sendOrderCreate(order.getId(), order.getStatus().toString());
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderOutput createOrderDgs(InputOrder orderRequest) {
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setDeliverer(delivererService.getDelivererById(UUID.fromString(orderRequest.delivererId())));
        orderRepository.save(order);
        rabbitSendService.sendOrderCreate(order.getId(), order.getStatus().toString());
        return modelMapper.map(order, OrderOutput.class);
    }


    @Override
    public OrderResponse updateOrder(UUID id, UpdateOrderRequest updatedOrder) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));

        String oldStatus = order.getStatus().toString();
        order.setOrderDate(updatedOrder.getOrderDate());
        order.setCustomerName(updatedOrder.getCustomerName());
        order.setCustomerAddress(updatedOrder.getCustomerAddress());
        order.setOrderCost(updatedOrder.getOrderCost());
        order.setDeliveryInstructions(updatedOrder.getDeliveryInstructions());
        order.setDeliveryDate(updatedOrder.getDeliveryDate());
        order.setStatus(Status.valueOf(updatedOrder.getStatus().toString()));
        order.setDeliverer(delivererService.getDelivererById(updatedOrder.getDelivererId()));
        order.setCustomerLatitude(updatedOrder.getCustomerLatitude());
        order.setCustomerLongitude(updatedOrder.getCustomerLongitude());
        order.setDelivererLatitude(updatedOrder.getDelivererLatitude());
        order.setDelivererLongitude(updatedOrder.getDelivererLongitude());

        orderRepository.save(order);

        String newStatus = order.getStatus().toString();
        if (!oldStatus.equals(newStatus)) {
            rabbitSendService.sendOrderStatusUpdate(id, newStatus);
        }

        return modelMapper.map(order, OrderResponse.class);
    }


    @Override
    public OrderResponse updateStatus(UUID id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));

        String oldStatus = order.getStatus().toString();
        order.setStatus(Status.valueOf(status));

        String newStatus = order.getStatus().toString();
        if (!oldStatus.equals(newStatus)) {
            rabbitSendService.sendOrderStatusUpdate(id, newStatus);
        }

        orderRepository.save(order);
        return modelMapper.map(order, OrderResponse.class);
    }
    @Override
    public OrderOutput updateOrderDgs(UUID id, InputOrder updatedOrder) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));

        String oldStatus = order.getStatus().toString();
        order.setOrderDate(LocalDateTime.parse(updatedOrder.orderDate()));
        order.setCustomerName(updatedOrder.customerName());
        order.setCustomerAddress(updatedOrder.customerAddress());
        order.setOrderCost(updatedOrder.orderCost());
        order.setDeliveryInstructions(updatedOrder.deliveryInstructions());
        order.setDeliveryDate(LocalDateTime.parse(updatedOrder.deliveryDate()));
        order.setStatus(Status.valueOf(updatedOrder.status().toString()));
        order.setDeliverer(delivererService.getDelivererById(UUID.fromString(updatedOrder.delivererId())));
        order.setCustomerLatitude(updatedOrder.customerLatitude());
        order.setCustomerLongitude(updatedOrder.customerLongitude());
        order.setDelivererLatitude(updatedOrder.delivererLatitude());
        order.setDelivererLongitude(updatedOrder.delivererLongitude());
        orderRepository.save(order);

        String newStatus = order.getStatus().toString();
        if (!oldStatus.equals(newStatus)) {
            rabbitSendService.sendOrderStatusUpdate(id, newStatus);
        }

        return modelMapper.map(order, OrderOutput.class);
    }

    public void updateDelivererCoordinates(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));

        double latitudeStep = (order.getCustomerLatitude() - order.getDelivererLatitude()) * 0.1;
        double longitudeStep = (order.getCustomerLongitude() - order.getDelivererLongitude()) * 0.1;

        order.setDelivererLatitude(order.getDelivererLatitude() + latitudeStep);
        order.setDelivererLongitude(order.getDelivererLongitude() + longitudeStep);

        orderRepository.save(order);
        System.out.println("КООРДИНАТЫ ДОСТАВЩИКА ПОМЕНЯЛИСЬ!!!");

        rabbitSendService.sendDelivererCoordinatesUpdate(order.getId(), order.getDelivererLatitude(), order.getDelivererLongitude(), order.getCustomerLatitude(), order.getDelivererLongitude());
    }

    @Override
    public boolean deleteOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));

        orderRepository.delete(order);
        return true;
    }
}
