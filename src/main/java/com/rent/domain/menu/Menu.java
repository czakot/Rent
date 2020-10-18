/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

import com.rent.domain.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author czakot
 */
@Component
public class Menu {
    private final List<MenuItem> menuItems = new ArrayList<>();
    
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    private MenuItem selectedMenuItem;

    public MenuItem getSelectedMenuItem() {
        return selectedMenuItem;
    }

    public void setSelectedMenuItem(MenuItem selectedMenuItem) {
        this.selectedMenuItem = selectedMenuItem;
    }

    public Menu() {
        MenuItem menuItem;
        menuItem = new MenuItem("Notice board", "noticeboard");
        menuItems.add(menuItem);
        
        menuItem = new MenuItem("User profile", "userprofile");
        menuItems.add(menuItem);

        menuItem = new MenuItem("Dashboard", "dashboard");
        menuItems.add(menuItem);

        selectedMenuItem = menuItems.get(0);
    }
    
    public void setMenu(Role role) {
        
    }
    
    public void setSelectedMenuItem(String targetUri) {
        for(MenuItem menuItem : menuItems) {
            if (menuItem.getContentPageUri().equals(targetUri)) {
                selectedMenuItem = menuItem;
                break;
            }
        }
    }
    
}
