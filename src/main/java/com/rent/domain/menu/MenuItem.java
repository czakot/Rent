package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.*;

class MenuItem extends MenuBaseElement{
    
    private final List<Tab> tabs = new ArrayList<>();
    private Tab selectedTab = null;
    private final Map<String, Tab> tabCache;

    // display strings: (key = reference, def, eng, hu) => resources/messages/menu_messages_...properties

    MenuItem(String reference, String controllerUri, Set<Role> availableForRoles) {
        super(reference, controllerUri != null ? controllerUri : '/' + reference, availableForRoles);
        tabCache = new HashMap<>();
    }

    void addTab(Tab tab) {
        tabs.add(tab);
        tabCache.put(tab.getControllerUri(), tab);
    }

    @Override
    protected void setAvailableByRole(Role role) {
        super.setAvailableByRole(role);
        for (Tab tab : tabs) {
            if (isAvailable()) {
                tab.setAvailableByRole(role);
            } else {
                tab.setAvailable(false);
            }
        }
        selectedTab = getFirstAvailableTab();
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public Tab getSelectedTab() {
        return selectedTab;
    }

    void setSelectedTabByControllerUri(String controllerUri) {
        selectedTab = tabCache.get(controllerUri);
        assert selectedTab != null;
    }

    private Tab getFirstAvailableTab() {
        return tabs.stream().filter(MenuBaseElement::isAvailable).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MenuItem{" + super.toString());
        if (tabs.isEmpty()) {
            sb.append(", selectedTab=").append(selectedTab).append(", tab={").append(tabs).append("}}");
        } else {
            sb.append(", selectedTab=").append(selectedTab == null ? selectedTab : selectedTab.getReference()).append(", tabs={");
            for (Tab tab : tabs) {
                sb.append("\n          ").append(tab);
            }
            sb.append("}}");
        }
        return sb.toString();
    }
}
