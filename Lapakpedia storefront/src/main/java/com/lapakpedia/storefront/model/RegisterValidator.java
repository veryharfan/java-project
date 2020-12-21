/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapakpedia.storefront.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 *
 * @author Toshiba
 */
@Component
public class RegisterValidator {
    
    private Boolean validateUserNameAsEmailAddress(Register model){
        String emailPattern = "^(.+)@(\\S+)$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(model.getUsername());
        return matcher.matches();
        
    }
    
    private Boolean validatePasswordAndConfirmPasswordIsTheSame(Register model){
        return model.getPassword().equals(model.getConfirmPassword())
                && model.getPassword().length() > 0;        
    }
    
    public Boolean validate(Register model){
        return validateUserNameAsEmailAddress(model) &&
                validatePasswordAndConfirmPasswordIsTheSame(model);
    }
}
