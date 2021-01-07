package com.rent.entity;

import com.rent.domain.Role;

import javax.persistence.*;

@Entity
@Table(name = "matchers")
public class Matcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @ManyToOne
    private MenuNode menuNode;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Matcher() {
    }

    public Matcher(MenuNode menuNode, Role role) {
        this.menuNode = menuNode;
        this.role = role;
    }

    public MenuNode getMenuNode() {
        return menuNode;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Matcher{" +
                "key=" + key +
                ", menuNode=" + menuNode +
                ", role=" + role +
                '}';
    }

}
