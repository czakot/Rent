/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.entity.htmlmessage;

/**
 *
 * @author czakot
 */
public enum MessageType {
    WARNING("alert alert-warning"),
    SUCCESS("alert alert-success"),
    DANGER("alert alert-danger");
    
    public final String cssClass;

    private MessageType(String cssClass) {
        this.cssClass = cssClass;
    }
}
