/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.exception;

/**
 *
 * @author czakot
 */
public class MissingActivationCodeInUriException extends RuntimeException {
    
    private final String sourceUri;

    public String getSourceUri() {
        return sourceUri;
    }

    public MissingActivationCodeInUriException(String message, String sourceUri) {
        super(message);
        this.sourceUri = sourceUri;
    }

}
