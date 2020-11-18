/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.authmessage;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author czakot
 */
@Component
public final class AuthMessages {
    
    private final List<AuthMessage> authMessageList = new ArrayList<>();
    
    private MessageSource messageSource;

    public AuthMessages() {
    }
    
    public AuthMessages(String messageKey, MessageType messageType) {
        add(messageKey, messageType);
    }
    
    public void add(String messageKey, MessageType messageType) {
        final AuthMessage authMessage = new AuthMessage(messageKey, messageType, messageSource);
        if (!authMessageList.contains(authMessage)) {
            authMessageList.add(authMessage);
        }
    }
    
    public void clearAndAddFirst(String messageKey, MessageType messageType) {
        clear();
        final AuthMessage authMessage = new AuthMessage(messageKey, messageType, messageSource);
        authMessageList.add(authMessage);
    }
    
    public void clear() {
        authMessageList.clear();
    }
    
    public List<AuthMessage> getAuthMessageList() {
        return authMessageList;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}