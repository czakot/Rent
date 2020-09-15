/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author czakot
 */
@Controller
public class RentController {
    
    UserService userService;

    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("adminExists", userService.adminExists());
        return "/index";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
