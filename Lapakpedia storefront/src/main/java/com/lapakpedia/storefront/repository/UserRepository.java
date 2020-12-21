/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.storefront.repository;

import com.lapakpedia.storefront.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Toshiba
 */
public interface UserRepository extends CrudRepository<User, Integer>{
    
    User findByUsername(String username);
    
}
