package com.rent.entity;


import com.rent.repo.MenuNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Configurable
@Table(name = "menunodehierarchy")
public class MenuNodeHierarchy implements Serializable {

    @Autowired
    private transient MenuNodeRepository menuNodeRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Embedded
    private MenuNode child;

    @Column(nullable = false)
    @Embedded
    private  MenuNode parent;

    public MenuNodeHierarchy() {}

    public MenuNodeHierarchy(String childReference, String parentReference) {
        child = menuNodeRepository.findMenuNodeByReference(childReference);
        parent = menuNodeRepository.findMenuNodeByReference(parentReference);
    }

    public MenuNode getChild() {
        return child;
    }

    public MenuNode getParent() {
        return parent;
    }

}
