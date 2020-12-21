package com.lapakpedia.storefront.controller;

import com.lapakpedia.storefront.entity.Product;
import com.lapakpedia.storefront.entity.User;
import com.lapakpedia.storefront.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class CacheProductRepository {

    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "findByUser")
    public List<Product> findByUser(User user){
        return productRepository.findByUser(user);
    }

    @Cacheable(value = "findById")
    public Product findById(Integer id){
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Cacheable(value = "findByNameContaining")
    public Page<Product> findByNameContaining(String query,
            Integer page,
            Integer size,
            String sort){
        return productRepository.findByNameContaining(query,
                builPageable(page, size, sort));
    }

    @Cacheable(value = "findByCategory")
    public Page<Product> findByCategory(String category,
            Integer page,
            Integer size,
            String sort){
        return productRepository.findByCategory(category,
                builPageable(page, size, sort));
    }

    private Pageable builPageable(Integer page, Integer size, String sort){
        Sort.Order order = null;
        if ("PRICE_DESC".equals(sort)){
            order = new Sort.Order(Sort.Direction.DESC, "price");
        } else if ("PRICE_ASC".equals(sort)){
            order = new Sort.Order(Sort.Direction.ASC, "price");
        } else if ("TITLE".equals(sort)){
            order = new Sort.Order(Sort.Direction.ASC, "name");
        }
        return PageRequest.of(page, size, Sort.by(order));
    }

}