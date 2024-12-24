package com.example.delivery2.services.impl;

import com.example.deliveryapi.dto.GraphQL.DelivererOutput;
import com.example.deliveryapi.dto.GraphQL.InputDeliverer;
import com.example.deliveryapi.dto.REST.CreateDelivererRequest;
import com.example.deliveryapi.dto.REST.DelivererResponse;
import com.example.deliveryapi.dto.REST.UpdateDelivererRequest;
import com.example.delivery2.models.Deliverer;

import com.example.delivery2.repositories.DelivererRepository;
import com.example.delivery2.services.IDelivererService;
import com.example.deliveryapi.exceptions.DelivererNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DelivererServiceImpl implements IDelivererService {
    private DelivererRepository delivererRepository;

    private ModelMapper modelMapper;
    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setDelivererRepository(DelivererRepository delivererRepository) {
        this.delivererRepository = delivererRepository;
    }


    @Override
    public List<DelivererResponse> showAllDeliverers() {
        List<Deliverer> deliverers = delivererRepository.findAll();
        return deliverers.stream()
                .map(order -> modelMapper.map(deliverers, DelivererResponse.class))
                .toList();
    }

    @Override
    public List<DelivererOutput> showAllDeliverersDgs() {
        List<Deliverer> deliverers = delivererRepository.findAll();
        return deliverers.stream()
                .map(order -> modelMapper.map(deliverers, DelivererOutput.class))
                .toList();
    }


    @Override
    public Deliverer getDelivererById(UUID id) {
        return delivererRepository.findById(id).orElse(null);
    }

    @Override
    public List<Deliverer> getAllDeliverers() {
        return delivererRepository.findAll();
    }

    @Override
    public DelivererResponse showDelivererById(UUID id) {
        Deliverer deliverer = delivererRepository.findById(id)
                .orElseThrow(() -> new DelivererNotFoundException("Deliverer with ID " + id + " not found"));
        return modelMapper.map(deliverer, DelivererResponse.class);
    }

    @Override
    public DelivererOutput showDelivererByIdDgs(UUID id) {
        Deliverer deliverer = delivererRepository.findById(id)
                .orElseThrow(() -> new DelivererNotFoundException("Deliverer with ID " + id + " not found"));
        return modelMapper.map(deliverer, DelivererOutput.class);
    }

    @Override
    public DelivererResponse createDeliverer(CreateDelivererRequest createdDeliverer) {
        Deliverer deliverer = modelMapper.map(createdDeliverer, Deliverer.class);
        deliverer.setRating(ThreadLocalRandom.current().nextDouble(4,5));
        delivererRepository.save(deliverer);
        return modelMapper.map(deliverer,DelivererResponse.class);
    }

    @Override
    public DelivererOutput createDelivererDgs(InputDeliverer createdDeliverer) {
        Deliverer deliverer = modelMapper.map(createdDeliverer, Deliverer.class);
        delivererRepository.save(deliverer);
        return modelMapper.map(deliverer,DelivererOutput.class);
    }

    @Override
    public DelivererResponse updateDeliverer(UUID id, UpdateDelivererRequest updatedDeliverer) {
        Deliverer deliverer = delivererRepository.findById(id)
                .orElseThrow(() -> new DelivererNotFoundException("Deliverer with ID " + id + " not found"));

        deliverer.setName(updatedDeliverer.getName());
        deliverer.setPhoneNumber(updatedDeliverer.getPhoneNumber());
        deliverer.setEmail(updatedDeliverer.getEmail());
        deliverer.setVehicleType(updatedDeliverer.getVehicleType());
        deliverer.setRating(updatedDeliverer.getRating());

        delivererRepository.save(deliverer);
        return modelMapper.map(deliverer, DelivererResponse.class);
    }

    @Override
    public DelivererOutput updateDelivererDgs(UUID id, InputDeliverer updatedDeliverer) {
        Deliverer deliverer = delivererRepository.findById(id)
                .orElseThrow(() -> new DelivererNotFoundException("Deliverer with ID " + id + " not found"));

        deliverer.setName(updatedDeliverer.name());
        deliverer.setPhoneNumber(updatedDeliverer.phoneNumber());
        deliverer.setEmail(updatedDeliverer.email());
        deliverer.setVehicleType(updatedDeliverer.vehicleType());
        deliverer.setRating(updatedDeliverer.rating());

        delivererRepository.save(deliverer);
        return modelMapper.map(deliverer, DelivererOutput.class);
    }

    @Override
    public boolean deleteDeliverer(UUID id) {
        Deliverer deliverer = delivererRepository.findById(id)
                .orElseThrow(() -> new DelivererNotFoundException("Deliverer with ID " + id + " not found"));

        delivererRepository.delete(deliverer);
        return true;
    }

}
