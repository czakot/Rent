package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.List;

public interface Menu {

    void setRole(Role role);
    void setSelectedMenuItemByControllerUri(String controllerUri);
    void setSelectedTabByControllerUri(String tabControllerUri);
    MenuItem getSelectedMenuItem();
    List<MenuItem> getMenuItems();
    Role getCurrentRole();

}
