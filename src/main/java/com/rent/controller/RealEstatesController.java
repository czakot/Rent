package com.rent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RealEstatesController {

    private static final String VIEW_PKG = getViewAutoPackageNameOfController();

    @RequestMapping({"/realestatesimport", "/realestateslist", "/realestatesmanualadd"})
    public String selectedMenu(HttpServletRequest request) {
        return "realestates" + request.getRequestURI();
    }

    private static String getViewAutoPackageNameOfController() {
        String className = RealEstatesController.class.getSimpleName();
        int indexOfWordController = className.lastIndexOf("Controller");
        return className.substring(0, indexOfWordController).toLowerCase();
    }

}
