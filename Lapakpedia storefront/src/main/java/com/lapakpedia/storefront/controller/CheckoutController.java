package com.lapakpedia.storefront.controller;

import com.lapakpedia.storefront.entity.Product;
import com.lapakpedia.storefront.model.AddToCart;
import com.lapakpedia.storefront.repository.CartRepositoy;
import com.lapakpedia.storefront.repository.ProductRepository;
import com.lapakpedia.storefront.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/add-to-cart")
    private ResponseEntity<String> addToCart(@RequestBody AddToCart addToCart, Principal principal) {
        return ResponseEntity.ok(cartService.addItemToCart(addToCart, principal.getName()));
    }

    @Transactional
    @PostMapping("/checkout")
    private ResponseEntity<String> checkout(Principal principal){
        return ResponseEntity.ok(cartService.checkout(principal.getName()));
    }
}
