package com.rent.service;

import com.rent.domain.Role;
import com.rent.entity.User;
import com.rent.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    
    private static final boolean ACTIVATED = true;
    private static final Random random = new Random();

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
    public UserDetails loadUserByUsername(String username) {
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

    // todo check key duplication that a key never be used again
    // todo length settings from properties, but a minimum be built in
    // todo only hash should be stored, use as a password
    // todo expiry date
    public String generateKey() {
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

}