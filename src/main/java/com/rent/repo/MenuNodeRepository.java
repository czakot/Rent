package com.rent.repo;

import com.rent.entity.MenuNode;
import org.springframework.data.repository.CrudRepository;

public interface MenuNodeRepository extends CrudRepository<MenuNode, Long> {

    MenuNode findMenuNodeByReference(String reference);
}
