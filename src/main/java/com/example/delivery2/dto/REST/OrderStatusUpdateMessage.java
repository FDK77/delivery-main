package com.example.delivery2.dto.REST;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderStatusUpdateMessage {
    private UUID orderId;
    private String status;
    private String date;

    public OrderStatusUpdateMessage(UUID orderId, String status, String date) {
        this.orderId = orderId;
        this.status = status;
        this.date = date;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
