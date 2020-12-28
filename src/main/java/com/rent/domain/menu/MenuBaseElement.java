package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.ArrayList;
import java.util.List;

public class MenuBaseElement {

    private boolean available;
    protected final String reference;
    protected final String controllerUri;
    protected final List<Role> availableForRoles;

    MenuBaseElement(String reference, String controllerUri, List<Role> availableForRoles) {
        available = false;
        this.reference = reference;
        this.controllerUri = controllerUri;
        this.availableForRoles = new ArrayList<>();
        if (availableForRoles != null) {
            this.availableForRoles.addAll(availableForRoles);
        }
    }

    MenuBaseElement(String reference, List<Role> availableForRoles) {
        this(reference, '/' + reference, availableForRoles);
    }

    public void setAvailableByRole(Role role) {
        available = availableForRoles.contains(role);
    }

    public boolean isAvailable() {
        return available;
    }

    public String getReference() {
        return reference;
    }

    public String getControllerUri() {
        return controllerUri;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
