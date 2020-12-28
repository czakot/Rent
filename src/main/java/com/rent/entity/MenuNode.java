package com.rent.entity;

import com.rent.domain.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Embeddable
@Table(name = "menunodes")
public class MenuNode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Column(unique = true) // default: nullable = true
    private String controllerUri;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "matchers",
            joinColumns = @JoinColumn(referencedColumnName = "reference", unique = false))
    @Column(name = "id", nullable = false)
    @Enumerated(EnumType.STRING)
    private final Set<Role> roles = EnumSet.noneOf(Role.class);

    public MenuNode() {}

    public MenuNode(String reference, String controllerUri) {
        this.reference = reference;
        this.controllerUri = controllerUri;
    }

    public String getReference() {
        return reference;
    }

    public String getControllerUri() {
        return controllerUri;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
