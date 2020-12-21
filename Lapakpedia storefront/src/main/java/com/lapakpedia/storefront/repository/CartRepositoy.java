package com.lapakpedia.storefront.repository;

import com.lapakpedia.storefront.entity.Cart;
import com.lapakpedia.storefront.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface CartRepositoy extends CrudRepository<Cart, Integer> {

    Cart findByUserAndStatus(User user, String status);
}
