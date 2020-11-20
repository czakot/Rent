package com.rent.service;

import com.rent.entity.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    
    public User findByEmail(String email);
    
    public boolean registerUser(User user);
    
    public User userActivation(String code);
    
    public boolean adminExists();
    
    public boolean hasAdminNotActivated();

    public String getSelectedRoleOfAuthenticatedUser(Authentication authentication);

    public void setSelectedRoleOfAuthenticatedUser(Authentication authentication, String roleName);
    
}
