package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.List;

public class Tab extends MenuBaseElement{

    Tab(String uri, String reference, List<Role> availableFor) {
        super(uri, reference, availableFor);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
