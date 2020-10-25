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
    
    private final String displayNameKey;

    public String getDisplayNameKey() {
        return displayNameKey;
    }
    private final String contentPageUri;

    public String getContentPageUri() {
        return contentPageUri;
    }

//    boolean switchedOn;
//    boolean clickable;
//    String controller;
//    List<MenuItem> subMenu = new ArrayList<>();

    public MenuItem(String contentPageUri) {
        this.displayNameKey = contentPageUri;
        this.contentPageUri = contentPageUri;
    }
    
    public MenuItem(String displayName, String contentPageUri) {
        this.displayNameKey = displayName;
        this.contentPageUri = contentPageUri;
    }
    
//    public MenuItem(String displayName, boolean clickable, String controller) {
//        this.displayName = displayName;
//        this.clickable = clickable;
//        this.controller = controller;
//    }
    
    
}
