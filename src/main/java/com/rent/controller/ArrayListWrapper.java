package com.rent.controller;

import com.rent.domain.authmessage.AuthMessage;

import java.util.ArrayList;

public class ArrayListWrapper {

    private ArrayList<AuthMessage> messages;

    public ArrayList<AuthMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<AuthMessage> messages) {
        this.messages = messages;
    }
}
