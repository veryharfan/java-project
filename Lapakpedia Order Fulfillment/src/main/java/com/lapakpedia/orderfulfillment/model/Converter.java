package com.lapakpedia.orderfulfillment.model;

import com.lapakpedia.orderfulfillment.entity.Order;
import com.lapakpedia.orderfulfillment.entity.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converter {

    public Order convert(Cart cart){
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus("RECEIVED_IN_BACKSTORE");
        order.setCartId(cart.getId());
        order.setUserId(cart.getUser().getId());

        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItems(orderItems);

        int totalPrice = 0;
        for (CartItem item : cart.getCartItem()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(item.getProduct().getId());
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
            totalPrice += item.getPrice() * item.getQuantity();
        }
        order.setTotalPrice(totalPrice);
        return order;
    }
}
