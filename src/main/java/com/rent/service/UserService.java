package com.rent.service;

import com.rent.entity.Role;
import com.rent.entity.User;

public interface UserService {
    
    public User findByEmail(String email);
    
    public boolean registerUser(User user);
    
    public void registerMaster(User adminToRegister);
    
    public int numberOfUsers(Role role, boolean validated);

    public User userActivation(String code);
    
    public boolean enabledMasterExists();
    
    public boolean notValidatedMasterExists();
    
    public void deleteNotValidatedMaster();
    
    public boolean isMaster(User user);
    
}
