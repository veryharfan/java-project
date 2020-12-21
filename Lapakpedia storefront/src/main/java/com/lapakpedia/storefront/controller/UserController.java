/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.storefront.controller;

import com.lapakpedia.storefront.entity.User;
import com.lapakpedia.storefront.model.Register;
import com.lapakpedia.storefront.model.RegisterValidator;
import com.lapakpedia.storefront.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Toshiba
 */
@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RegisterValidator validator;
    
    @PostMapping("/register")
    private String register(@RequestBody Register model) {
        if (validator.validate(model)) {
            User user = userRepository.findByUsername(model.getUsername());
            if (user == null) {
                User newUser = new User();
                newUser.setUsername(model.getUsername());
                newUser.setName(model.getName());
                newUser.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
                userRepository.save(newUser);
                return "ok";
            }
            return "your user name already exist";
        }
        return "fail";
    }    
}
