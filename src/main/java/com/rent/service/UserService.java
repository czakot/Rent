package com.rent.service;

import com.rent.entity.User;

public interface UserService {
    
    public User findByEmail(String email);
    
    public boolean registerUser(User user);
    
//    public void registerMaster(User adminToRegister);
    
    public int numberOfUsers(String role, boolean activated);
    
    public boolean getAdminExist();

    public User userActivation(String code);
    
//    public boolean enabledMasterExists();
//    
//    public boolean notValidatedMasterExists();
//    
//    public void deleteNotValidatedMaster();
//    
//    public boolean isMaster(User user);
    
}
