package com.lapakpedia.storefront.service.impl;

import com.lapakpedia.storefront.AppConfig;
import com.lapakpedia.storefront.entity.Cart;
import com.lapakpedia.storefront.entity.CartItem;
import com.lapakpedia.storefront.entity.Product;
import com.lapakpedia.storefront.entity.User;
import com.lapakpedia.storefront.model.AddToCart;
import com.lapakpedia.storefront.repository.CartRepositoy;
import com.lapakpedia.storefront.repository.ProductRepository;
import com.lapakpedia.storefront.repository.UserRepository;
import com.lapakpedia.storefront.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepositoy cartRepositoy;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public String addItemToCart(AddToCart addToCart, String username) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepositoy.findByUserAndStatus(user, "ACTIVE");
        if (cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart.setTransactionDate(new Date());
            cart.setStatus("ACTIVE");
            List<CartItem> items = new ArrayList<>();
            cart.setCartItem(items);
        }
        Product product = productRepository.findById(addToCart.getProductId()).get();
        if (product == null){
            return "Product not found";
        }
        if (product.getStock() < addToCart.getQuantity()){
            return "Product out of stock";
        }
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setPrice(product.getPrice());
        item.setQuantity(addToCart.getQuantity());
        item.setCart(cart);
        cart.getCartItem().add(item);
        cartRepositoy.save(cart);
        return "ok";
    }

    @Override
    @Transactional
    public String checkout(String username)  {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepositoy.findByUserAndStatus(user, "ACTIVE");
        if (cart != null) {
            // reduce product stock
            List<CartItem> items = cart.getCartItem();
            for (CartItem item : items) {
                Product product = item.getProduct();
                if (product.getStock() - item.getQuantity() < 0){
                    LOG.info("Product " + product.getName() + " has not sufficient stock");
                    return "checkout fail";
                }
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            }
            // change cart status
            cart.setStatus("PROCESSED");
            // sending message to rabbit MQ
            LOG.info("sending message to AMQP");
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.convertAndSend("lapakpedia-storefront", cart);
            //save to database after changing status
            cartRepositoy.save(cart);
            return "ok";
        }
        return "cart not found";
    }
}