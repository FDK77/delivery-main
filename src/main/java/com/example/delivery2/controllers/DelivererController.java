package com.example.delivery2.controllers;

import com.example.deliveryapi.controllers.DelivererApi;
import com.example.deliveryapi.dto.REST.CreateDelivererRequest;
import com.example.deliveryapi.dto.REST.DelivererResponse;
import com.example.deliveryapi.dto.REST.UpdateDelivererRequest;
import com.example.delivery2.models.Deliverer;
import com.example.delivery2.services.impl.DelivererServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deliverers")
public class DelivererController implements DelivererApi {

    private DelivererServiceImpl delivererService;

    @Autowired
    public void setDelivererService(DelivererServiceImpl delivererService) {
        this.delivererService = delivererService;
    }
    @Override
    @GetMapping
    public List<DelivererResponse> getAllDeliverers() {
        return delivererService.showAllDeliverers();
    }
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DelivererResponse>> getDeliverer(@PathVariable UUID id) {
        DelivererResponse deliverer = delivererService.showDelivererById(id);
        EntityModel<DelivererResponse> resource = EntityModel.of(deliverer);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DelivererController.class).getAllDeliverers()).withRel("all-deliverers"));
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DelivererController.class).deleteDeliverer(id)).withRel("delete"));
        return ResponseEntity.ok(resource);
    }
    @Override
    @PostMapping
    public DelivererResponse createDeliverer(@RequestBody CreateDelivererRequest deliverer) {
        return delivererService.createDeliverer(deliverer);
    }
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<DelivererResponse>> updateDeliverer(@PathVariable UUID id, @RequestBody UpdateDelivererRequest deliverer) {
        DelivererResponse updatedDeliverer = delivererService.updateDeliverer(id, deliverer);
        EntityModel<DelivererResponse> resource = EntityModel.of(updatedDeliverer);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DelivererController.class).getDeliverer(id)).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DelivererController.class).getAllDeliverers()).withRel("all-deliverers"));
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DelivererController.class).deleteDeliverer(id)).withRel("delete"));
        return ResponseEntity.ok(resource);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliverer(@PathVariable UUID id) {
        delivererService.deleteDeliverer(id);
        return ResponseEntity.noContent().build();
    }

}