/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.storefront.controller;

import com.lapakpedia.storefront.model.Register;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Toshiba
 */
public class UserControllerTest {
    
    @Test
    public void registerPassword() {
        Register model = new Register();
        model.setUsername("very@gmail.com");
        model.setPassword("pwd");
        model.setConfirmPassword("pwd");
        
    }
    
}
