package com.rent.service;

import com.rent.entity.User;

public interface UserService {
    
    User findByEmail(String email);
    
    boolean registerUser(User user);
    
    User userActivation(String code);
    
    boolean adminExists();
    
    boolean hasAdminNotActivated();

}
