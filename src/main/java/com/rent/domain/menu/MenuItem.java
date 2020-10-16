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
    
    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }
    private final String contentPage;

    public String getContentPage() {
        return contentPage;
    }
//    boolean switchedOn;
//    boolean clickable;
//    String controller;
//    List<MenuItem> subMenu = new ArrayList<>();

    public MenuItem(String displayName, String contentPage) {
        this.displayName = displayName;
        this.contentPage = contentPage;
    }
    
//    public MenuItem(String displayName, boolean clickable, String controller) {
//        this.displayName = displayName;
//        this.clickable = clickable;
//        this.controller = controller;
//    }
    
    
}
