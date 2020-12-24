package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.ArrayList;
import java.util.List;

public class MenuBaseElement {
    protected boolean available;
    protected final String reference;
    protected final String controlerUri;
    protected final List<Role> availableForRoles;

    public void setAvailableByRole(Role role) {
        available = availableForRoles.contains(role);
    }

    MenuBaseElement(String reference, String controlerUri, List<Role> availableForRoles) {
        available = false;
        this.reference = reference;
        this.controlerUri = controlerUri;
        this.availableForRoles = new ArrayList<>();
        for (Role role : availableForRoles) {
            this.availableForRoles.add(role);
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public String getReference() {
        return reference;
    }

    public String getControlerUri() {
        return controlerUri;
    }
}
