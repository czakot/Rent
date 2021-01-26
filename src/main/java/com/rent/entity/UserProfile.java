package com.rent.entity;

import com.rent.domain.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return user.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(user, that.user);
    }
}
