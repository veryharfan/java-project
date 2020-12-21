package com.lapakpedia.orderfulfillment.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lapakpedia.orderfulfillment.entity.Order;
import com.lapakpedia.orderfulfillment.model.Cart;
import com.lapakpedia.orderfulfillment.model.Converter;
import com.lapakpedia.orderfulfillment.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "lapakpedia-storefront")
@Component
public class CartReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(CartReceiver.class);

    @Autowired
    private OrderRepository orderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Converter converter = new Converter();

    @RabbitHandler
    public void receive(byte[] message){
        try {
            String messageBody = new String(message);
            Cart cart = objectMapper.readValue(messageBody, Cart.class);
            Order order = converter.convert(cart);
            orderRepository.save(order);
        } catch (JsonProcessingException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
