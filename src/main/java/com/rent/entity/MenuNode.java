package com.rent.entity;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "menunodes")
public class MenuNode implements Serializable {

    @Id
    private String reference;

    @Column(unique = true) // default: nullable = true
    private String controllerUri;

    @ManyToOne
    private MenuNode parent;

    @OneToMany(mappedBy = "menuNode")
    private Set<Matcher> matchers = new HashSet<>();

    public MenuNode() {}

    @Autowired
    public MenuNode(String reference, String controllerUri, MenuNode parent) {
        this.reference = reference;
        this.controllerUri = controllerUri;
        this.parent = parent;
    }

    public boolean hasMatcherAnyRole() {
        return !(matchers.size() == 1 && matchers.stream().map(Matcher::getRole).anyMatch(Objects::isNull));
    }

    public String getReference() {
        return reference;
    }

    public String getControllerUri() {
        return controllerUri;
    }

    public MenuNode getParent() {return parent; }

    public Set<Matcher> getMatchers() {
        return matchers;
    }
}
