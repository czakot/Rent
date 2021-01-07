package com.rent.entity;

import com.rent.domain.Role;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userprofiles")
public class UserProfile implements Serializable {

    @Id
    @OneToOne
    private User user;

    private Role preferredInitialRole = null;

    public UserProfile() {
    }

    public Role getPreferredInitialRole() {
        return preferredInitialRole;
    }

    public void setPreferredInitialRole(Role preferredInitialRole) {
        this.preferredInitialRole = preferredInitialRole;
    }
}
