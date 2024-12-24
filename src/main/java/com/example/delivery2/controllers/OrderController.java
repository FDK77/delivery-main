package com.example.delivery2.controllers;

import com.example.deliveryapi.controllers.OrderApi;
import com.example.deliveryapi.dto.REST.CreateOrderRequest;
import com.example.deliveryapi.dto.REST.OrderResponse;
import com.example.deliveryapi.dto.REST.UpdateOrderRequest;
import com.example.delivery2.models.Order;
import com.example.delivery2.services.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderApi {

    private OrderServiceImpl orderService;

    @Autowired
    public void setOrderService(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }
    @Override
    @GetMapping
    public List<OrderResponse> getAllOrders() {return orderService.showAllOrders();}
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrderResponse>> getOrder(@PathVariable UUID id) {
        OrderResponse order = orderService.showOrderById(id);
        EntityModel<OrderResponse> resource = EntityModel.of(order);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getAllOrders()).withRel("all-orders"));
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).deleteOrder(id)).withRel("delete-order"));
        return ResponseEntity.ok(resource);
    }
    @Override
    @PostMapping
    public ResponseEntity<EntityModel<OrderResponse>> createOrder(@RequestBody CreateOrderRequest orderRequest) {

        OrderResponse savedOrderResponse = orderService.createOrder(orderRequest);
        EntityModel<OrderResponse> resource = EntityModel.of(savedOrderResponse);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getOrder(savedOrderResponse.getId())).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getAllOrders()).withRel("all-orders"));
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).deleteOrder(savedOrderResponse.getId())).withRel("delete-order"));
        return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getOrder(savedOrderResponse.getId())).toUri()).body(resource);
    }
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<OrderResponse>> updateOrder(@PathVariable UUID id, @RequestBody UpdateOrderRequest order) {
        OrderResponse updatedOrder = orderService.updateOrder(id, order);
        EntityModel<OrderResponse> resource = EntityModel.of(updatedOrder);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getOrder(id)).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).deleteOrder(id)).withRel("delete"));
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getAllOrders()).withRel("all-orders"));
        return ResponseEntity.ok(resource);
    }

    @Override
    @PutMapping("/status/{id}/{status}")
    public ResponseEntity<EntityModel<OrderResponse>> changeStatus(@PathVariable UUID id, @PathVariable String status) {
        OrderResponse order = orderService.updateStatus(id, status);
        EntityModel<OrderResponse> resource = EntityModel.of(order);
        return ResponseEntity.ok(resource);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
