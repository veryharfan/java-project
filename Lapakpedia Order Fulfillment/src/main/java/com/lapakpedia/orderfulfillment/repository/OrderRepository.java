package com.lapakpedia.orderfulfillment.repository;

import com.lapakpedia.orderfulfillment.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    
    Order findByIdAndStatus(Integer id, String status);
    
}
