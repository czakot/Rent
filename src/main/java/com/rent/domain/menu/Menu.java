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

    public static final List<Role> allRole = Arrays.asList(Role.values());

    private final List<MenuItem> menuItems = new ArrayList<>();
    private MenuItem selectedMenuItem;

    public Menu() {
        MenuItem menuItem;

        menuItems.add(new MenuItem("realestates", allRole));
        menuItems.add(new MenuItem("noticeboard", allRole));
        menuItems.add(new MenuItem("userprofile", allRole));
        menuItems.add(new MenuItem("dashboard", allRole));
        selectedMenuItem = menuItems.get(0);
    }
    
    public void setForRole(Role role) {
        for (MenuItem menuItem : menuItems) {
            menuItem.setAvailableByRole(role);
        }
    }
    
    public void setSelectedMenuItem(String displayReference) {
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getReference().equals(displayReference)) {
                selectedMenuItem = menuItem;
                break;
            }
        }
    }

    public MenuItem getSelectedMenuItem() {
        return selectedMenuItem;
    }

    public void setSelectedMenuItem(MenuItem selectedMenuItem) {
        this.selectedMenuItem = selectedMenuItem;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

}
