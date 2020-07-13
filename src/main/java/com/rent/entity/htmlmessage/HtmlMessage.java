/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.entity.htmlmessage;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author czakot
 */
public class HtmlMessage {
    
    private final String text;

    public String getText() {
        return text;
    }

    private final String cssClass;

    public String getCssClass() {
        return cssClass;
    }
    
    public HtmlMessage(String messageKey, MessageType messageType, MessageSource messageSource) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        this.text = messageSource.getMessage(messageKey, null, currentLocale);
        this.cssClass = messageType.cssClass;
    }
    
    public String toJson() {
        StringBuilder json = new StringBuilder();
        addJsonPair(json, "text", text);
        addJsonPair(json, "cssClass", cssClass);

        json.insert(0, "{");
        json.append('}');
        
        return json.toString();
    }
    
    private void addJsonPair(StringBuilder json, String varname, String value) {
        if (json.length() != 0) {
            json.append(", ");
        }
        json.append("\"").append(varname).append("\":\"").append(value).append("\"");
    }
}
