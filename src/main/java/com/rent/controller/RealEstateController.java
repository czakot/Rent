package com.rent.controller;

import com.rent.domain.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/realestate")
public class RealEstateController {

    private Menu menu;

    @RequestMapping({ "/list","/import", "/manualadd"})
    public String selectedMenu(HttpServletRequest request, Model model) {
        String requestUri = request.getRequestURI();
        menu.setSelectedTabByControllerUri(requestUri);
        model.addAttribute("menuItems", menu.getMenuItems());
        model.addAttribute("selectedMenuItem", menu.getSelectedMenuItem());
        return requestUri;
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
