package com.lapakpedia.storefront.security.controller;

import com.lapakpedia.storefront.entity.User;
import com.lapakpedia.storefront.model.Login;
import com.lapakpedia.storefront.repository.UserRepository;
import com.lapakpedia.storefront.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/api/login")
    private ResponseEntity<String> loginAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            Login login = new ObjectMapper().readValue(req.getInputStream(), Login.class);
            User user = userRepository.findByUsername(login.getUsername());
            if (user == null || 
                    !passwordEncoder.matches(login.getPassword(), user.getPassword())){
                return ResponseEntity.ok("Username Or Password wrong");
            }
            String token = jwtService.generateToken(user.getUsername());
            res.setHeader("token", token);
            return ResponseEntity.ok("Welcome " + user.getUsername());
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }

    }

}
