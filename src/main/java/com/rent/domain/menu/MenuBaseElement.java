package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.HashSet;
import java.util.Set;

class MenuBaseElement {

    private boolean available;
    protected final String reference;
    protected final String controllerUri;
    protected final Set<Role> availableForRoles;

    protected MenuBaseElement(String reference, String controllerUri, Set<Role> availableForRoles) {
        available = false;
        this.reference = reference;
        this.controllerUri = controllerUri != null ? controllerUri : '/' + reference;
        assert availableForRoles != null;
        this.availableForRoles = new HashSet<>(availableForRoles);
    }

    protected void setAvailable(boolean available) {
        this.available = available;
    }

    protected void setAvailableByRole(Role role) {
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

    @Override
    public String toString() {
        return "reference='" + reference + '\'' +
                ", available=" + available +
                ", controllerUri='" + controllerUri + '\'' +
                ", availableForRoles=" + availableForRoles +
                '}';
    }
}
