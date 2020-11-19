package com.rent.domain;

import java.util.Collections;
import java.util.Set;

public enum Role {
    ADMIN,
    OWNER,
    TENANT,
    TRUSTEE,
    OBSERVER;

    public static Role getMostWeightedRoleFromSet(Set<Role> roleSet) {
        return Collections.min(roleSet);
    }
    
    public static Role getRoleFromSetOfRolesByRoleName(Set<Role> roleSet, String roleName) {
        Role role;
        try {
            role = Role.valueOf(roleName);
            if (!roleSet.contains(role)) {
                role = null;
            }
        } catch (IllegalArgumentException | NullPointerException ex) {
            role = null;
        }
        return role;
    }
    
}
