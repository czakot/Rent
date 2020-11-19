/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

import com.rent.domain.Role;
import java.util.ArrayList;
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
        menuItem = new MenuItem("noticeboard");
        menuItems.add(menuItem);
        
        menuItem = new MenuItem("userprofile");
        menuItems.add(menuItem);

        menuItem = new MenuItem("dashboard");
        menuItems.add(menuItem);

        selectedMenuItem = menuItems.get(0);
    }
    
    public void setMenuByRole(Role role) {
        
    }
    
    public void setSelectedMenuItem(String targetRef) {
        for(MenuItem menuItem : menuItems) {
            if (menuItem.getContentPageRef().equals(targetRef)) {
                selectedMenuItem = menuItem;
                break;
            }
        }
    }
    
}
