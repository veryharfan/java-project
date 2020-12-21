/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.storefront.receiver;

import com.lapakpedia.storefront.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lapakpedia.storefront.entity.Cart;
import com.lapakpedia.storefront.repository.CartRepositoy;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Toshiba
 */
@RabbitListener(queues = "lapakpedia-orderfulfillment")
@Component
public class OrderReceiver {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderReceiver.class);
    
    @Autowired
    private CartRepositoy cartRepositoy;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @RabbitHandler
    public void changeCartStatusAfterOrderFulfillment(byte[] message){
        try {
            String messageBody = new String(message);
            LOG.info("message body= " + messageBody);
            Order order = objectMapper.readValue(messageBody, Order.class);
            LOG.info("Order = " + messageBody);
            Cart cart = cartRepositoy.findById(order.getCartId()).get();
            cart.setStatus(order.getStatus());
            cartRepositoy.save(cart);
        } catch (JsonProcessingException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
