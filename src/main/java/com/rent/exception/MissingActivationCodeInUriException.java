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

    private String sourceUri;

    public MissingActivationCodeInUriException(String sourceUri) {
        super(String.format("Activation link (%s) incomplete, missing activation code.", sourceUri));
        this.sourceUri = sourceUri;
    }

    public String getSourceUri() {
        return sourceUri;
    }

}
