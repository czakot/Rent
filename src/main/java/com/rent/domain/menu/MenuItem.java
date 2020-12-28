/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author czakot
 */
public class MenuItem extends MenuBaseElement{
    
    private final List<Tab> tabs = new ArrayList<>();
    private Tab selectedTab = null;

    // display strings: (key = reference, def, eng, hu) => resources/messages/menu_messages_...properties

    public MenuItem(String reference, List<Role> availableForRoles) {
        super(reference, '/' + reference,  availableForRoles);
    }
    
    public MenuItem(String reference, String controllerUri, List<Role> availableForRoles) {
        super(reference, controllerUri, availableForRoles);
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
    }

    public void addTabForSameRoles(Tab tab) {
        Tab tabSameRolesInserted = new Tab(tab.getReference(), tab.getControllerUri(), new ArrayList<>(availableForRoles));
        addTab(tabSameRolesInserted);
    }

    @Override
    public void setAvailableByRole(Role role) {
        super.setAvailableByRole(role);
        for (Tab tab : tabs) {
            if (isAvailable()) {
                tab.setAvailableByRole(role);
            } else {
                tab.setAvailable(false);
            }
        }
        selectedTab = getInitialSelectedTab();
    }

    private Tab getInitialSelectedTab() {
        return tabs.stream().filter(tab -> tab.isAvailable()).findFirst().orElse(null);
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public Tab getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTabByControllerUri(String controllerUri) {
        selectedTab = tabs.stream().filter(tab -> tab.getControllerUri().equals(controllerUri)).findFirst().orElse(null);
    }
}
