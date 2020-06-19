/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.entity.htmlmessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author czakot
 */
@Component
public class HtmlMessageFactory {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
        
    public HtmlMessage createHtmlMessage(String messageKey, MessageType messageType) {
        return new HtmlMessage(messageKey, messageType, messageSource);
    }
    
}
