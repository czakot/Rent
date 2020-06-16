/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.entity;

import com.rent.RentApplication;
import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 *
 * @author czakot
 */
public class HtmlMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    private String cssClass;

    public String getCssClass() {
        return cssClass;
    }
    
    public HtmlMessage(String messageKey, MessageType messageType/*, MessageSource messageSource*/) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        ResourceBundleMessageSource messageSource = RentApplication.getContext().getBean(ResourceBundleMessageSource.class);
        this.message = messageSource.getMessage(messageKey, null, currentLocale);
        this.cssClass = messageType.cssClass;
    }
}
