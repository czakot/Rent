/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.entity.utility;

import com.rent.entity.Role;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author czakot
 */
public class RoleUtility {
    // weighted list of roles
    private static final String[] roleNames = {"ADMIN", "OWNER", "TENANT", "TRUSTEE", "USER"};
    
    public static Role getMostWeightedRole(Set<Role> roleSet) {
        if (roleSet.size() == 1) {
            return (Role)roleSet.toArray()[0];
        }
        HashMap<String, Role> roles = new HashMap<>();
        roleSet.forEach((role) -> {roles.put(role.getRole(), role);});
        Role role = null;
        for(String roleName : roleNames) {
            role = roles.get(roleName);
            if( role != null) {
                break;
            }
        }
        return role;
    }
    
    public static Role getRoleFromSetByRoleName(Set<Role> roleSet, String roleName) {
        Role roleByName = null;
        for(Role role : roleSet) {
            if (role.getRole().equals(roleName)) {
                roleByName = role;
                break;
            }
        }
        return roleByName;
    }
}
