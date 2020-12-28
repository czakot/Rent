package com.rent.repo;

import com.rent.entity.MenuNode;
import com.rent.entity.MenuNodeHierarchy;
import org.springframework.data.repository.CrudRepository;

public interface MenuNodeHierarchyRepository extends CrudRepository<MenuNodeHierarchy, Long> {
}
