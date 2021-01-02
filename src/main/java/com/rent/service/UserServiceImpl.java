package com.rent.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rent.domain.Role;
import com.rent.entity.User;
import com.rent.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    
    private static final boolean ACTIVATED = true;
    private int numberOfActiveAdmins;

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        numberOfActiveAdmins = numberOfUsersByRoleAndActivation(Role.ADMIN, ACTIVATED);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean registerUser(User userToRegister) {
        boolean registered;
        if (adminExists()) {
            registered = register(userToRegister, Role.OBSERVER);
        } else {
            userRepository.deleteAll();
            registered = register(userToRegister, Role.ADMIN);
        }

        if (registered) {
            emailService.sendMessage(userToRegister);
        }
        return registered;
    }

    private int numberOfUsersByRoleAndActivation(Role role, boolean activated) {
        return userRepository.countUsersByRoleAndActivation(role.name(), activated);
    }
    
    @Override
    public boolean adminExists() {
        return numberOfActiveAdmins > 0;
    }

    private boolean register(User user, Role role) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }
        user.addRole(role);
        user.setEnabled(false);
        user.setActivation(generateKey());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public String generateKey() {
        String key = "";
        Random random = new Random();
        char[] word = new char[16];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(26));
        }
        return new String(word);
    }

    @Override
    public User userActivation(String code) {
        User user = userRepository.findByActivation(code);
        if (user != null) {
            user.setEnabled(true);
            user.setActivation("");
            userRepository.save(user);
            if (user.getRoles().contains(Role.ADMIN)) {
                numberOfActiveAdmins++;
            }
        }
        return user;
    }

    @Override
    public boolean hasAdminNotActivated() {
        return numberOfUsersByRoleAndActivation(Role.ADMIN, !ACTIVATED) > 0;
    }

    @Override
    public String getSelectedRoleNameOfAuthenticatedUser(Authentication authentication) {
        return ((GrantedAuthority)((UserDetailsImpl)authentication.getPrincipal()).getAuthorities().toArray()[0]).getAuthority().substring(5); // substring => clipping "ROLE_" from beginning
//        return ((GrantedAuthority)((UserDetailsImpl)authentication.getPrincipal()).getAuthorities().toArray()[0]).getAuthority();
    }

    @Override
    public void setSelectedRoleOfAuthenticatedUser(Authentication authentication, String roleName) {
        ((UserDetailsImpl)authentication.getPrincipal()).setUserSelectedRoleByName(roleName);
    }

}