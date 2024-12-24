package com.example.delivery2.models;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private LocalDateTime orderDate;
    private String customerName;
    private String customerAddress;
    private double orderCost;
    private String deliveryInstructions;
    private LocalDateTime deliveryDate;
    @Column(name = "customer_latitude")
    private double customerLatitude;

    @Column(name = "customer_longitude")
    private double customerLongitude;

    @Column(name = "deliverer_latitude")
    private double delivererLatitude;

    @Column(name = "deliverer_longitude")
    private double delivererLongitude;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "deliverer_id", nullable = false)
    private Deliverer deliverer;

    @Column(name = "order_date")
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Column(name = "customer_name")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "customer_address")
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Column(name = "order_cost")
    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }

    @Column(name = "delivery_instructions")
    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    @Column(name = "delivery_date")
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }

    public Status getStatus() {
        return status;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }

    public double getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(double customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public double getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(double customerLongitude) {
        this.customerLongitude = customerLongitude;
    }

    public double getDelivererLatitude() {
        return delivererLatitude;
    }

    public void setDelivererLatitude(double delivererLatitude) {
        this.delivererLatitude = delivererLatitude;
    }

    public double getDelivererLongitude() {
        return delivererLongitude;
    }

    public void setDelivererLongitude(double delivererLongitude) {
        this.delivererLongitude = delivererLongitude;
    }
}
