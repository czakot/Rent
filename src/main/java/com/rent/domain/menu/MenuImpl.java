/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

import com.rent.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author czakot
 */
@Component
@SessionScope
public class MenuImpl implements Menu {

    private final List<MenuItem> menuItems;
    private MenuItem selectedMenuItem;
    private Role currentRole;
    private final Map<String, MenuItem> menuItemCache;

    @Autowired
    public MenuImpl(MenuInitTree menuInitTree) {

        menuItems = new ArrayList<>();
        selectedMenuItem = null;
        currentRole = null;
        menuItemCache = new HashMap<>();

        for (MenuInitValueNode menuNode : menuInitTree) {
            if (menuNode.getParent() == null) {
                addMenuItem(new MenuItem(
                        menuNode.getReference(),
                        menuNode.getControllerUri(),
                        menuNode.getAvailableForRoles()));
            } else {
                if (menuNode.getParent() != null) {
                    Tab tab = new Tab(
                            menuNode.getReference(),
                            menuNode.getControllerUri(),
                            menuNode.getAvailableForRoles());
                    addTabToMenuItem(menuNode.getParent().getReference(), tab);
                }
            }
        }
    }

    @Override
    public void setRole(Role role) {
        if (this.currentRole != role) {
            this.currentRole = role;
            menuItems.forEach(menuItem -> menuItem.setAvailableByRole(role));
            selectedMenuItem = getInitialSelectedMenuItem();
        }
    }

    @Override
    public void setSelectedMenuItemByControllerUri(String controllerUri) {
        selectedMenuItem = menuItemCache.get(controllerUri);
    }

    @Override
    public void setSelectedTabByControllerUri(String tabControllerUri) {
        selectedMenuItem.setSelectedTabByControllerUri(tabControllerUri);
    }

    @Override
    public MenuItem getSelectedMenuItem() {
        return selectedMenuItem;
    }

    @Override
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public Role getCurrentRole() {
        return currentRole;
    }

    private MenuItem getInitialSelectedMenuItem() {
        MenuItem selected = getPreferredSelectedMenuItem();
        if (selected == null) {
            selected = getFirstAvailableMenuItem();
        }
        assert selected != null;
        return selected;
    }

    private MenuItem getFirstAvailableMenuItem() {
        return menuItems.stream().filter(MenuBaseElement::isAvailable).findFirst().orElse(null);
    }

    private MenuItem getPreferredSelectedMenuItem() {
        //todo getPreferredSelectedMenuItem
        return null;
    }

    private void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        menuItemCache.put(menuItem.getReference(), menuItem);
        menuItemCache.put(menuItem.getControllerUri(), menuItem);
    }

    private void addTabToMenuItem(String menuItemReference, Tab tab) {
        menuItemCache.get(menuItemReference).addTab(tab);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                "Menu{currentRole = " + currentRole + " selectedMenuItem = " + selectedMenuItem.getReference() + '\n'
        );
        for (MenuItem menuItem : menuItems) {
            sb.append("    ").append(menuItem).append('\n');
        }
        sb.append('}');

        return sb.toString();
    }
}
