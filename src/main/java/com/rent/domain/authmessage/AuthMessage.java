/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.authmessage;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author czakot
 */
public class AuthMessage {
    
    private final String text;

    public String getText() {
        return text;
    }

    private final String cssClass;

    public String getCssClass() {
        return cssClass;
    }
    
    public AuthMessage(String messageKey, MessageType messageType, MessageSource messageSource) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        this.text = messageSource.getMessage(messageKey, null, currentLocale);
        this.cssClass = messageType.cssClass;
    }
    
    @Override
    public boolean equals(Object obj) {
        // compare by content only
            if(obj == this) {
                return true;
            }
            if(obj == null) {
                return false;
            }
            if(getClass() != obj.getClass()) {
                return false;
            }
            AuthMessage r = (AuthMessage)obj;
            return this.text.equals(r.getText()) && this.cssClass.equals(r.getCssClass());
    }
}
