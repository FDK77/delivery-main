package com.example.delivery2.datafetchers;

import com.example.deliveryapi.datafetchers.OrderDataFetcherApi;
import com.example.deliveryapi.dto.GraphQL.DelivererOutput;
import com.example.deliveryapi.dto.GraphQL.InputOrder;
import com.example.deliveryapi.dto.GraphQL.OrderOutput;
import com.example.delivery2.models.Deliverer;
import com.example.delivery2.models.Order;
import com.example.delivery2.services.impl.DelivererServiceImpl;
import com.example.delivery2.services.impl.OrderServiceImpl;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
public class OrderDataFetcher implements OrderDataFetcherApi {
    private OrderServiceImpl orderService;

    private DelivererServiceImpl delivererService;

    @Autowired
    public void setOrderService(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setDelivererService(DelivererServiceImpl delivererService) {
        this.delivererService = delivererService;
    }
    @Override
    @DgsQuery
    public List<OrderOutput> orders(@InputArgument String customerNameFilter) {
        List<OrderOutput> orders = orderService.showAllOrdersDgs();
        if (customerNameFilter == null || customerNameFilter.isEmpty()) {
            return orders;
        }
        return orders.stream()
                .filter(order -> order.getCustomerName().toLowerCase().contains(customerNameFilter.toLowerCase()))
                .collect(Collectors.toList());
    }
    @Override
    @DgsMutation
    public OrderOutput addOrder(@InputArgument("order") InputOrder orderInput) {
        return orderService.createOrderDgs(orderInput);
    }
    @Override
    @DgsMutation
    public OrderOutput updateOrder(@InputArgument String id, @InputArgument("order") InputOrder orderInput) {
        UUID uuid = UUID.fromString(id);
        return orderService.updateOrderDgs(uuid, orderInput);
    }
    @Override
    @DgsMutation
    public boolean deleteOrder(@InputArgument("id") String id) {
        return orderService.deleteOrder(UUID.fromString(id));
    }
}
