package com.example.delivery2.services;

import com.example.deliveryapi.dto.GraphQL.DelivererOutput;
import com.example.deliveryapi.dto.GraphQL.InputDeliverer;
import com.example.deliveryapi.dto.REST.CreateDelivererRequest;
import com.example.deliveryapi.dto.REST.DelivererResponse;
import com.example.deliveryapi.dto.REST.UpdateDelivererRequest;
import com.example.delivery2.models.Deliverer;

import java.util.List;
import java.util.UUID;

public interface IDelivererService {

    DelivererResponse updateDeliverer(UUID id, UpdateDelivererRequest updatedDeliverer);

    DelivererOutput updateDelivererDgs(UUID id, InputDeliverer updatedDeliverer);

    List<DelivererResponse> showAllDeliverers();

    List<DelivererOutput> showAllDeliverersDgs();

    Deliverer getDelivererById(UUID id);

    List<Deliverer> getAllDeliverers();


    DelivererResponse showDelivererById(UUID id);

    DelivererOutput showDelivererByIdDgs(UUID id);

    DelivererResponse createDeliverer(CreateDelivererRequest deliverer);

    DelivererOutput createDelivererDgs(InputDeliverer deliverer);

    boolean deleteDeliverer(UUID id);
}
