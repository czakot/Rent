package com.rent.entity;

import java.util.Set;


public enum Role {
    ADMIN,
    OWNER,
    TENANT,
    TRUSTEE,
    USER;

    public static Role getMostWeightedRole(Set<Role> roleSet) {
        return (Role)roleSet.toArray()[0];
    }
    
    public static Role getRoleFromSetOfRolesByRoleName(Set<Role> roleSet, String roleName) {
        Role role = Role.valueOf(roleName);
        return roleSet.contains(role) ? role : null;
    }

}
