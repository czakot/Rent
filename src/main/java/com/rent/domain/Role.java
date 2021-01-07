package com.rent.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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

    public static Set<Role> getAllRoleSet() {
        return new HashSet<>(Arrays.asList(values()));
    }

    public static String[] getAllRoleNameArray() {
        return Arrays.stream(values()).map(Enum::name).toArray(String[]::new);
    }

}
