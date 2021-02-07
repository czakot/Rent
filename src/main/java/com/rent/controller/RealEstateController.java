package com.rent.controller;

import com.rent.domain.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/realestate")
public class RealEstateController {

    private Menu menu;

    @RequestMapping({ "/list","/import", "/manualadd"})
    public String selectedMenu(HttpServletRequest request, Model model) {
        return request.getRequestURI();
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @ModelAttribute("menu")
    public Menu propagateChangedMenu(HttpServletRequest request) {
        menu.setSelectedTabByControllerUri(request.getRequestURI());
        return menu;
    }
}
