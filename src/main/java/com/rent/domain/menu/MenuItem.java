/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.menu;

/**
 *
 * @author czakot
 */
public class MenuItem {
    
    private final String contentPageRef;

    public String getContentPageRef() {
        return contentPageRef;
    }

//    boolean switchedOn;
//    boolean clickable;
//    String controller;
//    List<MenuItem> subMenu = new ArrayList<>();

    public MenuItem(String contentPageRef) {
        // display strings:(key = contentPageRef, def, eng, hu) => resources/messages/menu_messages_...propertirs
        this.contentPageRef = contentPageRef;
    }
    
//    public MenuItem(String displayName, boolean clickable, String controller) {
//        this.displayName = displayName;
//        this.clickable = clickable;
//        this.controller = controller;
//    }
    
    
}
