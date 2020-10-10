/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain;

/**
 *
 * @author czakot
 */
public class UserRegistrationDto {

        private String regFullName;
        private String regEmail;
        private String regPassword;

    public String getRegFullName() {
        return regFullName;
    }

    public void setRegFullName(String regFullName) {
        this.regFullName = regFullName;
    }

    public String getRegEmail() {
        return regEmail;
    }

    public void setRegEmail(String regEmail) {
        this.regEmail = regEmail;
    }

    public String getRegPassword() {
        return regPassword;
    }

    public void setRegPassword(String regPassword) {
        this.regPassword = regPassword;
    }
    
    @Override
    public String toString() {
        return "[" + regFullName + "; " + regEmail + "; " + regPassword + "]";
    }
}
