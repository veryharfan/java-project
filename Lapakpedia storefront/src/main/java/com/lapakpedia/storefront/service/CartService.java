package com.lapakpedia.storefront.service;

import com.lapakpedia.storefront.model.AddToCart;
import org.springframework.transaction.annotation.Transactional;

public interface CartService {

    public String addItemToCart(AddToCart addToCart, String username);

    public String checkout(String username);
}
