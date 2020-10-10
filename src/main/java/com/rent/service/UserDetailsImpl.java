package com.rent.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.rent.domain.Role;
import com.rent.entity.User;

public class UserDetailsImpl implements UserDetails, UserAuthorityDetails {

    private static final long serialVersionUID = 3185970362329652822L;

    private final User user;
    private Role selectedRole;

    public UserDetailsImpl(User user) {
        this.user = user;
        selectedRole = getUserMostWeightedRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<GrantedAuthority> authorities = new HashSet<>();
            if (!user.getRoles().contains(selectedRole)) {
                selectedRole = getUserMostWeightedRole();
            }
            authorities.add(new SimpleGrantedAuthority(selectedRole.name()));
        return authorities;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAvailableAuthorities() {
            Collection<GrantedAuthority> authorities = new HashSet<>();
            Set<Role> roles = user.getRoles();
            roles.forEach((role) -> {
                authorities.add(new SimpleGrantedAuthority(role.name()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    public String getFullName() {
        return user.getFullName();
    }
    
    private Role getUserMostWeightedRole() {
        return Role.getMostWeightedRole(user.getRoles());
    }

    public void setUserSelectedRoleByName(String roleName) {
        selectedRole = Role.getRoleFromSetOfRolesByRoleName(user.getRoles(), roleName);
        if (selectedRole == null) {
            selectedRole = getUserMostWeightedRole();
        }
    }

}
