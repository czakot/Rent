/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootContentController {

    @RequestMapping({"/noticeboard*", "/dashboard"})
    public String selectedMenu(HttpServletRequest request) {
        return "rootcontent" + request.getRequestURI();
    }

}
