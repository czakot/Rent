/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.entity.htmlmessage;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.MessageSource;

/**
 *
 * @author czakot
 */
public final class HtmlMessages {
    
    private final List<HtmlMessage> htmlMessageList = new ArrayList<>();
    
    private final MessageSource messageSource;
    
    public HtmlMessages(String messageKey, MessageType messageType, MessageSource messageSource) {
        this.messageSource = messageSource;
        add(messageKey, messageType);
    }
    
    public HtmlMessages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    public void add(String messageKey, MessageType messageType) {
        final HtmlMessage htmlMessage = new HtmlMessage(messageKey, messageType, messageSource);
        htmlMessageList.add(htmlMessage);
    }
    
    public void clearAndAddFirst(String messageKey, MessageType messageType) {
        clear();
        final HtmlMessage htmlMessage = new HtmlMessage(messageKey, messageType, messageSource);
        htmlMessageList.add(htmlMessage);
    }
    
    public void clear() {
        htmlMessageList.clear();
    }
    
    public List<HtmlMessage> getHtmlMessageList() {
        return htmlMessageList;
    }
}