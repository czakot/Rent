/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author czakot
 */
@Controller
public class NoticeboardController {
    
    @RequestMapping({"/noticeboard_1", "/noticeboard_2"})
    public String noticeboard(Model model, HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("X-Frame-Options", "SAMEORIGIN");
        String target = request.getRequestURI().substring(1);
        return target;
    }

    
}
