package com.example.delivery2.datafetchers;

import com.example.deliveryapi.datafetchers.DelivererDataFetcherApi;
import com.example.deliveryapi.dto.GraphQL.DelivererOutput;
import com.example.deliveryapi.dto.GraphQL.InputDeliverer;
import com.example.deliveryapi.dto.REST.CreateDelivererRequest;
import com.example.delivery2.models.Deliverer;
import com.example.delivery2.services.impl.DelivererServiceImpl;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
public class DelivererDataFetcher implements DelivererDataFetcherApi {
    private DelivererServiceImpl delivererService;

    @Autowired
    public void setDelivererService(DelivererServiceImpl delivererService) {
        this.delivererService = delivererService;
    }
    @Override
    @DgsQuery
    public List<DelivererOutput> deliverers(@InputArgument String nameFilter) {
        List<DelivererOutput> deliverers = delivererService.showAllDeliverersDgs();
        if (nameFilter == null || nameFilter.isEmpty()) {
            return deliverers;
        }
        return deliverers.stream()
                .filter(deliverer -> deliverer.getName().toLowerCase().contains(nameFilter.toLowerCase()))
                .collect(Collectors.toList());
    }
    @Override
    @DgsMutation
    public DelivererOutput addDeliverer(@InputArgument InputDeliverer deliverer) {
        return delivererService.createDelivererDgs(deliverer);
    }
    @Override
    @DgsMutation
    public DelivererOutput updateDeliverer(@InputArgument String id, @InputArgument("deliverer") InputDeliverer delivererInput) {
        UUID uuid = UUID.fromString(id);
        return delivererService.updateDelivererDgs(uuid,delivererInput);
    }
    @Override
    @DgsMutation
    public boolean deleteDeliverer(@InputArgument("id") String id) {
        return delivererService.deleteDeliverer(UUID.fromString(id));
    }
}
