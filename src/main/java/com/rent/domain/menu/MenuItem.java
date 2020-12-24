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
    
    // reference = contentPageRef;

    List<Tab> tabs = new ArrayList<>();

    public MenuItem(String reference, List<Role> availableFor) {
        super('/' + reference, reference,  availableFor);
        // display strings:(key = reference, def, eng, hu) => resources/messages/menu_messages_...properties
    }
    
    public MenuItem(String uri, String displayReference, List<Role> availableFor) {
        super(uri, displayReference, availableFor);
        // display strings:(key = reference, def, eng, hu) => resources/messages/menu_messages_...properties
    }

    @Override
    public void setAvailableByRole(Role role) {
        super.setAvailableByRole(role);
        for (Tab tab : tabs) {
            if (available) {
                tab.setAvailableByRole(role);
            } else {
                tab.setAvailable(false);
            }
        }
    }
    
}
