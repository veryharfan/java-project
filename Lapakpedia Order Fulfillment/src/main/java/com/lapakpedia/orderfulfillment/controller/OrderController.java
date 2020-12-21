package com.lapakpedia.orderfulfillment.controller;

import com.lapakpedia.orderfulfillment.entity.Order;
import com.lapakpedia.orderfulfillment.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/{orderId}")
    private ResponseEntity<String> setStatusToDelivered(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.shipping(orderId));
    }
}
