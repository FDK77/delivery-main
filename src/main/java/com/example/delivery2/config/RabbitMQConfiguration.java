package com.example.delivery2.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    static final String exchangeName = "testExchange";

    @Bean
    public Queue discountQueue() {
        return new Queue("discountQueue", false);
    }
    @Bean
    public Queue coordinatesQueue() {
        return new Queue("coordinatesQueue", false);
    }
    @Bean
    public Queue auditDiscountQueue() {
        return new Queue("auditDiscountQueue", false);
    }
    @Bean
    public Queue auditCoordinatesQueue() {
        return new Queue("auditCoordinatesQueue", false);
    }
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName, false, false);
    }

    @Bean
    public Binding discountBinding(Queue discountQueue, TopicExchange exchange) {
        return BindingBuilder.bind(discountQueue).to(exchange).with("my.key");
    }

    @Bean
    public Binding coordinatesBinding(Queue coordinatesQueue, TopicExchange exchange) {
        return BindingBuilder.bind(coordinatesQueue).to(exchange).with("deliverer.coordinates.update");
    }
    @Bean
    public Binding auditDiscountBinding(Queue auditDiscountQueue, TopicExchange exchange) {
        return BindingBuilder.bind(auditDiscountQueue).to(exchange).with("my.key");
    }

    @Bean
    public Binding auditCoordinatesBinding(Queue auditCoordinatesQueue, TopicExchange exchange) {
        return BindingBuilder.bind(auditCoordinatesQueue).to(exchange).with("deliverer.coordinates.update");
    }
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
