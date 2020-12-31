package com.rent.controller;

import com.rent.domain.menu.Menu;
import com.rent.domain.menu.MenuImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RealEstatesController {

    private static final String VIEW_PKG = getViewAutoPackageNameOfController(RealEstatesController.class);

    private MenuImpl menu;

    @RequestMapping({ "/realestatelist","/realestateimport", "/realestatemanualadd"})
    public String selectedMenu(HttpServletRequest request, Model model) {
        String requestUri = request.getRequestURI();
        menu.setSelectedTabByControllerUri(requestUri);
        model.addAttribute("menuItems", menu.getMenuItems());
        model.addAttribute("selectedMenuItem", menu.getSelectedMenuItem());
        return VIEW_PKG + requestUri;
    }

    private static String getViewAutoPackageNameOfController(Class<?> aClass) {
        String className = aClass.getSimpleName();
        int indexOfWordController = className.lastIndexOf("Controller");
        return className.substring(0, indexOfWordController).toLowerCase();
    }

    @Autowired
    public void setMenu(MenuImpl menu) {
        this.menu = menu;
    }
}
