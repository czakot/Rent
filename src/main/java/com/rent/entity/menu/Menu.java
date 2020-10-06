/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.entity.menu;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author czakot
 */
@Scope("session")
@Component
public class Menu {
    
    private static final String[] displayNames = {"Logs, messages", "User details"};
//    private static final String[] controller = {"/userinterests", "/userdetails"};
    
    private final List<MenuItem> menuItems = new ArrayList<>();

    public Menu() {
        for (String displayName : displayNames) {
            menuItems.add(new MenuItem(displayName));
        }
    }
    
}
