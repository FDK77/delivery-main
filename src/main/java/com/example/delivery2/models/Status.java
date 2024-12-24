package com.example.delivery2.models;

public enum Status {
    ASSEMBLING,    // В сборке
    IN_TRANSIT,    // В процессе доставки
    ARRIVED,       // Товар прибыл к получателю
    DELIVERED,     // Доставлен
    CANCELED       // Отменен
}
