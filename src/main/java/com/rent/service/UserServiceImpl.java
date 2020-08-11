package com.rent.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rent.entity.Role;
import com.rent.entity.User;
import com.rent.repo.RoleRepository;
import com.rent.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    
    private static final boolean ACTIVATED = true;
    private int numberOfActiveAdmins;

    private final String USER_ROLE = "USER";
    private final String ADMIN_ROLE = "ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        numberOfActiveAdmins = numberOfUsers(ADMIN_ROLE, ACTIVATED);
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
            registered = register(userToRegister, new String[]{USER_ROLE});
        } else {
            userRepository.deleteAll();
            registered = register(userToRegister, new String[]{ADMIN_ROLE});
        }

        if (registered) {
            emailService.sendMessage(userToRegister);
        }

        return registered;
    }

    private int numberOfUsers(String role, boolean activated) {
        return userRepository.countUsers(role, activated);
    }
    
    @Override
    public boolean adminExists() {
        return numberOfActiveAdmins > 0;
    }

    private boolean register(User user, String[] roles) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        for (String roleName : roles) {
            Role role = roleRepository.findByRole(roleName);
            if (role != null) {
                user.getRoles().add(role);
            } else {
                user.addRoles(roleName);
            }
        }

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
            if (user.getRoles().contains(new Role(ADMIN_ROLE))) {
                numberOfActiveAdmins++;
            }
        }

        return user;
    }

    @Override
    public boolean existsNotActivatedAdmin() {
        return userRepository.countUsers(ADMIN_ROLE, !ACTIVATED) > 0;
    }
    
}