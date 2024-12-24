package com.example.delivery2.dto.GraphQL;

public record InputDeliverer(String name,
                             String phoneNumber,
                             String email,
                             String vehicleType,
                             Float rating) {}
