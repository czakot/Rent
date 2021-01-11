package com.rent.entity;

import com.rent.domain.Role;
import com.rent.domain.UserRegistrationDto;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

// todo optional/default generated identifier
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

// todo first and last name separation (in registration too)
    private String fullName;

    private String activation;

    private Boolean enabled;

    // todo whether naming should be usersroles or usersxroles (in other taybles too)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
               joinColumns = @JoinColumn(name = "user_id")) // unique = false (default)
    @Column(name = "id", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = EnumSet.noneOf(Role.class);


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserProfile userProfile;
    
    public User() {}
    
    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
    
    public User(UserRegistrationDto registrationDto) {
        this.fullName = registrationDto.getRegFullName();
        this.email = registrationDto.getRegEmail();
        this.password = registrationDto.getRegPassword();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(EnumSet<Role> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User [id=" + id + " full name=" + fullName + ", email=" + email + ", password=" + password + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
