package com.example.delivery2;

import com.example.delivery2.models.Deliverer;
import com.example.delivery2.models.Order;
import com.example.delivery2.models.Status;
import com.example.delivery2.repositories.DelivererRepository;
import com.example.delivery2.services.impl.OrderServiceImpl;
import com.example.deliveryapi.dto.REST.CreateOrderRequest;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DelivererRepository delivererRepository;

    @Autowired
    private OrderServiceImpl orderService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {
        createDeliverers();
        // Имитация создания заказов
        startOrderCreationSimulation();
        // Имитация обновления статусов
        startOrderStatusSimulation();
        //Имитация движения доставщика
        startCoordinatesUpdateSimulation();
    }

    private void createDeliverers() {
        Deliverer deliverer1 = new Deliverer();
        deliverer1.setName("Иван Иванов");
        deliverer1.setPhoneNumber("+79001234567");
        deliverer1.setEmail("ivan@example.com");
        deliverer1.setVehicleType("Велосипед");
        deliverer1.setRating(4.5);

        Deliverer deliverer2 = new Deliverer();
        deliverer2.setName("Петр Петров");
        deliverer2.setPhoneNumber("+79009876543");
        deliverer2.setEmail("petr@example.com");
        deliverer2.setVehicleType("Машина");
        deliverer2.setRating(4.7);

        delivererRepository.save(deliverer1);
        delivererRepository.save(deliverer2);
    }

    private void startOrderCreationSimulation() {
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                createRandomOrder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, getRandomDelay(), TimeUnit.MILLISECONDS);
    }

    private void createRandomOrder() {

        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setCustomerName(faker.name().fullName());
        orderRequest.setCustomerAddress(faker.address().fullAddress());
        orderRequest.setOrderCost(ThreadLocalRandom.current().nextDouble(500, 5000));
        orderRequest.setDeliveryInstructions(faker.lorem().sentence());


        double customerLatitude = randomLatitudeInMoscow();
        double customerLongitude = randomLongitudeInMoscow();
        double delivererLatitude = randomLatitudeInMoscow();
        double delivererLongitude = randomLongitudeInMoscow();


        orderRequest.setCustomerLatitude(customerLatitude);
        orderRequest.setCustomerLongitude(customerLongitude);
        orderRequest.setDelivererLatitude(delivererLatitude);
        orderRequest.setDelivererLongitude(delivererLongitude);


        orderService.createOrder(orderRequest);
        System.out.println("Создан заказ для клиента: " + orderRequest.getCustomerName());
        System.out.println("Координаты клиента: " + customerLatitude + ", " + customerLongitude);
        System.out.println("Координаты доставщика: " + delivererLatitude + ", " + delivererLongitude);
    }


    private void startOrderStatusSimulation() {
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                List<Order> orders = orderService.getAllOrders().stream()
                        .filter(order -> order.getStatus() != Status.IN_TRANSIT)
                        .toList();

                for (Order order : orders) {
                    scheduleOrderStatusUpdate(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }


    private void scheduleOrderStatusUpdate(Order order) {
        scheduler.schedule(() -> {
            try {
                if (order.getStatus() != Status.DELIVERED && order.getStatus() != Status.CANCELED) {
                    Status nextStatus = getNextStatus(order.getStatus());
                    orderService.updateStatus(order.getId(), nextStatus.toString());
                    System.out.println("Обновлен статус заказа " + order.getId() + ": " + nextStatus);

                    if (nextStatus == Status.DELIVERED) {
                        orderService.deleteOrder(order.getId());
                        System.out.println("Удален заказ с ID: " + order.getId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, getRandomDelay(), TimeUnit.MILLISECONDS);
    }

    private Status getNextStatus(Status currentStatus) {
        switch (currentStatus) {
            case ASSEMBLING:
                return Status.IN_TRANSIT;
            case IN_TRANSIT:
                return Status.ARRIVED;
            case ARRIVED:
                return Status.DELIVERED;
            default:
                return currentStatus;
        }
    }

    private void startCoordinatesUpdateSimulation() {
        scheduler.scheduleWithFixedDelay(() -> {
            List<Order> activeOrders = orderService.getAllOrders().stream()
                    .filter(order -> order.getStatus() == Status.IN_TRANSIT)
                    .toList();

            for (Order order : activeOrders) {
                orderService.updateDelivererCoordinates(order.getId());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }


    private long getRandomDelay() {
        return ThreadLocalRandom.current().nextLong(1000, 60000);
    }

    private double randomLatitudeInMoscow() {
        return ThreadLocalRandom.current().nextDouble(55.55, 55.92);
    }

    private double randomLongitudeInMoscow() {
        return ThreadLocalRandom.current().nextDouble(37.36, 37.85);
    }

}
