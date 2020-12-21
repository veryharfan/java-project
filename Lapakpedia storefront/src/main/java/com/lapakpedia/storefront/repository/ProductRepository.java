/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.storefront.repository;

import com.lapakpedia.storefront.entity.Product;
import java.util.List;

import com.lapakpedia.storefront.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Toshiba
 */
public interface ProductRepository extends CrudRepository<Product, Integer>{

    List<Product> findByUser(User user);

    Product findByNameAndUser(String name, User user);

    Product findByIdAndUser(Integer id, User user);

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByNameContaining(String query, Pageable pageable);

}
