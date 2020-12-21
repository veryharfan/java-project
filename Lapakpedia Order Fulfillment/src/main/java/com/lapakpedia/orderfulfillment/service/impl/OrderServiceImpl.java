/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.orderfulfillment.service.impl;

import com.lapakpedia.orderfulfillment.AppConfig;
import com.lapakpedia.orderfulfillment.entity.Order;
import com.lapakpedia.orderfulfillment.repository.OrderRepository;
import com.lapakpedia.orderfulfillment.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Toshiba
 */
@Service
public class OrderServiceImpl implements OrderService{
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Override
    @Transactional
    public String shipping(Integer orderId) {
        Order order = orderRepository.findByIdAndStatus(orderId, "RECEIVED_IN_BACKSTORE");
        if (order == null) {
            return "Order not found";
        }
        // Change order status 
        order.setStatus("DELIVERED");
        // sending message to rabbit MQ
        LOG.info("sending order fulfillment message to AMQP");
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("lapakpedia-orderfulfillment", order);
        //save to database after changing status
        orderRepository.save(order);
        return "ok";
    }
        
}
