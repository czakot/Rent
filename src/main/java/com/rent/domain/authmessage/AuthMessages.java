/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.authmessage;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.MessageSource;

/**
 *
 * @author czakot
 */
public final class AuthMessages {
    
    private final List<AuthMessage> authMessageList = new ArrayList<>();
    
    private final MessageSource messageSource;
    
    public AuthMessages(String messageKey, MessageType messageType, MessageSource messageSource) {
        this.messageSource = messageSource;
        add(messageKey, messageType);
    }
    
    public AuthMessages(MessageSource messageSource) {
        this.messageSource = messageSource;
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
}