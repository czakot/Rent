/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author czakot
 */
public interface UserAuthorityDetails {

//    public Collection<? extends GrantedAuthority> getAvailableAuthorities();

    public String getRoleNameOfSelectedRole();
    public String[] getRoleNamesAvailable();
    public int getNumberOfAvailableRoles();

}
