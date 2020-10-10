/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author czakot
 */
@Component
public class Menu {
    
    private static final String[] DISPLAY_NAMES = {"Logs, messages", "User details"};
//    private static final String[] CONTROLLER = {"/userinterests", "/userdetails"};
    
    private final List<String> menuItems = new ArrayList<>();
//    private final List<MenuItem> menuItems = new ArrayList<>();

    public List<String> getMenuItems() {
//    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public Menu() {
        for (String displayName : DISPLAY_NAMES) {
            menuItems.add(displayName);
//            menuItems.add(new MenuItem(displayName));
        }
    }
    
}
