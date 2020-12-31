package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.Set;

class Tab extends MenuBaseElement{

    Tab(String reference, String controllerUri, Set<Role> availableForRoles) {
        super(reference, controllerUri, availableForRoles);
    }

    @Override
    public String toString() {
        return "Tab{" + super.toString() + "}";
    }
}
