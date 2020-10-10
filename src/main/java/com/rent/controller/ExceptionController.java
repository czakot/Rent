/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.exception.MissingActivationCodeInUriException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author czakot
 */
@Controller
@ControllerAdvice
public class ExceptionController {
    
    @ExceptionHandler(MissingActivationCodeInUriException.class)
    public String exceptionHandler(MissingActivationCodeInUriException ex, Model model) {
        model.addAttribute("sourceUri", ex.getSourceUri());
        model.addAttribute("exceptionMessage", ex.getMessage());
        return "/error/exceptionHandler";
    }
    
}
