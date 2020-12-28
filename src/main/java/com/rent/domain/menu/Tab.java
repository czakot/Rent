package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.List;

public class Tab extends MenuBaseElement{

    Tab(String reference, String controllerUri, List<Role> availableFor) {
        super(reference, controllerUri, availableFor);
    }

    Tab(String reference, List<Role> availableFor) {
        super(reference, availableFor);
    }

}
