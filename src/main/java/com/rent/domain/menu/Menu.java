/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.*;

import com.rent.entity.MenuNode;
import com.rent.repo.MenuNodeHierarchyRepository;
import com.rent.repo.MenuNodeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author czakot
 */
@Component
@SessionScope
public class Menu {

    @Autowired
    private MenuNodeRepository menuNodeRepository;

    @Autowired
    private MenuNodeHierarchyRepository menuNodeHierarchyRepository;

    private static final List<Role> allRoleExceptAdmin;
    static {
        allRoleExceptAdmin = new ArrayList<>(Arrays.asList(Role.values()));  // Arrays.asList() returns a fixed size List: next line would throw an exception
        allRoleExceptAdmin.remove(Role.ADMIN);
    }

    private final List<MenuItem> menuItems = new ArrayList<>();
    private MenuItem selectedMenuItem;
    private Role currentRole;
    private final Map<String, MenuItem> menuItemCache;

    public Menu() {



        addMenuItem(new MenuItem("realestates", new ArrayList<>(Arrays.asList(Role.values()))));
        addTabToMenuItem("realestates", new Tab("realestatelist", allRoleExceptAdmin ));
        addTabToMenuItem("realestates", new Tab("realestateimport", Arrays.asList(Role.ADMIN)));
        addTabToMenuItem("realestates", new Tab("realestatemanualadd", allRoleExceptAdmin));
        addMenuItem(new MenuItem("noticeboard", allRoleExceptAdmin));
        addMenuItem(new MenuItem("userprofile", allRoleExceptAdmin));
        addMenuItem(new MenuItem("dashboard", Arrays.asList(Role.ADMIN)));
        currentRole = null;
        menuItemCache = new HashMap<>();
        menuItems.forEach(menuItem -> menuItemCache.put(menuItem.getReference(), menuItem));
    }

    public void setForRole(Role role) {
        if (this.currentRole == role) {
            return;
        }
        menuItems.forEach(menuItem -> menuItem.setAvailableByRole(role));

        selectedMenuItem = getInitialSelectedMenuItem();
        this.currentRole = role;
    }

    private MenuItem getInitialSelectedMenuItem() {
        //todo getPreferredSelectedMenuItem
        return menuItems.stream().filter(menuItem -> menuItem.isAvailable()).findFirst().orElse(null);
    }

    public void setSelectedMenuItem(String reference) {
        selectedMenuItem = menuItemCache.get(reference);
    }

    public void setSelectedTabByControllerUri(String tabControllerUri) {
        selectedMenuItem.setSelectedTabByControllerUri(tabControllerUri);
    }

    private void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    private void addTabToMenuItem(String menuItemReference, Tab tab) {
        getMenuItemByReference(menuItemReference).addTab(tab);
    }

    private void addTabToMenuItemForSameRoles(String menuItemReference, Tab tab) {
        getMenuItemByReference(menuItemReference).addTabForSameRoles(tab);
    }

    private MenuItem getMenuItemByReference(String menuItemReference) {
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getReference().equals(menuItemReference)) {
                return menuItem;
            }
        }
        return null;
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
