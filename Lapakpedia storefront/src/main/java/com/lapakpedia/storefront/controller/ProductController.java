/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.storefront.controller;

import com.lapakpedia.storefront.entity.Product;
import com.lapakpedia.storefront.entity.User;
import com.lapakpedia.storefront.repository.ProductRepository;
import com.lapakpedia.storefront.repository.UserRepository;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Toshiba
 */
@RestController
@RequestMapping("/api")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheProductRepository cacheProductRepository;
    
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/products") //get all product data own by logged in user
    private ResponseEntity<List<Product>> getProducts(Principal principal){
        String userName = principal.getName();
        User user = userRepository.findByUsername(userName);
        return ResponseEntity.ok(cacheProductRepository.findByUser(user));
    }

    @GetMapping("/product/id/{id}") // get product data with id of 3
    private ResponseEntity<Product> getProductById(@PathVariable Integer id){
        return ResponseEntity.ok(cacheProductRepository.findById(id));
    }

    @GetMapping("/search") // search request
    private ResponseEntity<Map<String, Object>> searchProduct(
            @RequestParam String q,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort){
        return buildResponseEntity(cacheProductRepository.findByNameContaining(q, page, size, sort));
    }

    @GetMapping("/product") // get product by category
    private ResponseEntity<Map<String, Object>> getProductByCategory(
            @RequestParam String category,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort){
        return buildResponseEntity(cacheProductRepository.findByCategory(category, page, size, sort));
    }

    @PostMapping("/product") // post product by logged in user
    private ResponseEntity<String> addProduct(@RequestBody Product p, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        if (user != null) {
            Product product = productRepository.findByNameAndUser(p.getName(), user);
            if (product == null){
                p.setUser(user);
                productRepository.save(p);
                return ResponseEntity.ok("ok");
            }
            return ResponseEntity.ok("Product name already exist");
        }
        return ResponseEntity.ok("User not found");
    }

    @PutMapping("/product/{productId}") // update data with {id} by logged in user with json data in response body
    private ResponseEntity<String> updateProduct(@PathVariable Integer productId,
            @RequestBody Product product, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Product oldProductData = productRepository.findByIdAndUser(productId, user); // find original product data
        if (oldProductData != null){
            // Set new product data to old product data
            // if field in new product data at response body = null, old data not updated
            // Product category cannot be update because it means totally different product
            if (product.getName() != null){
                //Each username can't have same product name
                Product checkProductName = productRepository.findByNameAndUser(product.getName(), user);
                if (checkProductName != null && checkProductName != oldProductData){
                    return ResponseEntity.ok("Product name already exist");
                }
                oldProductData.setName(product.getName());
            }
            if (product.getDescription() != null){
                oldProductData.setDescription(product.getDescription());
            }
            if (product.getPrice() != null){
                oldProductData.setPrice(product.getPrice());
            }
            if (product.getStock() != null){
                oldProductData.setStock(product.getStock());
            }
            productRepository.save(oldProductData);
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.ok("Product not found");
    }

    @PutMapping("/product/stock/{productId}")
    private ResponseEntity<String> updateStockProduct(@PathVariable Integer productId,
            @RequestBody Integer stock, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Product product = productRepository.findByIdAndUser(productId, user); // find original product data
        if (product == null){
            return ResponseEntity.ok("Product not found");
        }
        product.setStock(stock);
        productRepository.save(product);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/product/{productId}")
    private ResponseEntity<String> deleteProduct(@PathVariable Integer productId, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Product product = productRepository.findByIdAndUser(productId, user);
        if (product == null){
            return ResponseEntity.ok("Product not found");
        }
        productRepository.delete(product);
        return ResponseEntity.ok("Success delete product with id " + productId);
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(Page<Product> productPage){
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPage", productPage.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}