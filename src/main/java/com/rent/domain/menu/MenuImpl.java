/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

import com.rent.domain.Role;
import com.rent.entity.Matcher;
import com.rent.entity.MenuNode;
import com.rent.repo.MenuNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author czakot
 */
@Component
@SessionScope
public class MenuImpl implements Menu {

    private static final boolean doPrint = false;
    private static final Set<Role> allRole = new HashSet<>(Arrays.asList(Role.values()));

    private final List<MenuItem> menuItems;
    private MenuItem selectedMenuItem;
    private Role currentRole;
    private final Map<String, MenuItem> menuItemCache;

    @Autowired
    public MenuImpl(MenuNodeRepository menuNodeRepository) {

        menuItems = new ArrayList<>();
        selectedMenuItem = null;
        currentRole = null;
        menuItemCache = new HashMap<>();

        for (MenuNode menuNode : menuNodeRepository.findAll()) {
            if (menuNode.getParent() == null) {
                addMenuItem(new MenuItem(
                        menuNode.getReference(),
                        menuNode.getControllerUri(),
                        menuNode.hasMatcherAnyRole() ?
                                menuNode.getMatchers().stream().map(Matcher::getRole).collect(Collectors.toSet()) :
                                allRole));
            }
        }
        for (MenuNode menuNode : menuNodeRepository.findAll()) {
            if (menuNode.getParent() != null) {
                Tab tab = new Tab(
                        menuNode.getReference(),
                        menuNode.getControllerUri(),
                        menuNode.hasMatcherAnyRole() ?
                                menuNode.getMatchers().stream().map(Matcher::getRole).collect(Collectors.toSet()) :
                                menuNode.getParent().getMatchers().stream().map(Matcher::getRole).collect(Collectors.toSet()));
                    addTabToMenuItem(menuNode.getParent().getReference(), tab);
            }
        }
    }

    @Override
    public void changeRoleTo(Role role) {
        if (this.currentRole != role) {
            this.currentRole = role;
            menuItems.forEach(menuItem -> menuItem.setAvailableByRole(role));
            selectedMenuItem = getInitialSelectedMenuItem();
        }

        if (doPrint) {
            System.out.println(this);
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
        //todo should return a deep copy
        return selectedMenuItem;
    }

    @Override
    public List<MenuItem> getMenuItems() {
        //todo should return a deep copy
        return menuItems;
    }

    private MenuItem getInitialSelectedMenuItem() {
        MenuItem selectedMenuItem = getPreferredSelectedMenuItem();
        if (selectedMenuItem == null) {
            selectedMenuItem = getFirstAvailableMenuItem();
        }
        assert selectedMenuItem != null;
        return selectedMenuItem;
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
