package com.rent.service;

import com.rent.entity.User;

public interface UserService {
    
    public User findByEmail(String email);
    
    public boolean registerUser(User user);
    
    public User userActivation(String code);
    
    public boolean adminExists();
    
    public boolean existsNotActivatedAdmin();
    
}
