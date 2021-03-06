/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain.authmessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author czakot
 */
// todo session scope
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
        addToMessageList(authMessage);
    }

    public void add(String messageKey, String insert, MessageType messageType) {
        add(messageKey, new String[]{insert}, messageType);
    }

    public void add(String messageKey, String[] inserts, MessageType messageType) {
        if (inserts != null) {
            final AuthMessage authMessage = new AuthMessage(messageKey, inserts, messageType, messageSource);
            addToMessageList(authMessage);
        } else {
            add(messageKey, messageType);
        }
    }

    private void addToMessageList(AuthMessage authMessage) {
        if (!authMessageList.contains(authMessage)) {
            authMessageList.add(authMessage);
        }
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