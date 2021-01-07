package com.rent.domain.menu;

import com.rent.domain.Role;
import com.rent.entity.MenuNode;
import com.rent.repo.MenuNodeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

@Component
public class MenuInitTree implements Iterable<MenuInitValueNode> {

    private final List<MenuInitValueNode> rootLevelNodes;

    @Autowired
    public MenuInitTree(MenuNodeRepository menuNodeRepository) {
        this.rootLevelNodes = new ArrayList<>();
        Map<String, MenuInitValueNode> cache = new HashMap<>();

        buildUpMenuInitTree(menuNodeRepository, cache);
        setControllerUrisDefaultsIfAbsentForAllTreeNode(cache);
    }

    private void buildUpMenuInitTree(MenuNodeRepository menuNodeRepository, Map<String, MenuInitValueNode> cache) {
        for (MenuNode menuNode : menuNodeRepository.findAllByOrderByCreationOrder()) {
            MenuInitValueNode initNode = new MenuInitValueNode(menuNode.getReference(),menuNode.getControllerUri());
            cache.put(initNode.getReference(), initNode);
            addToMenuInitTree(cache, menuNode, initNode);
            if (menuNode.hasMatcherAnyRole()) {
                initNode.setAvailableForRoles(menuNode.getRoles());
            } else {
                initNode.setAvailableForRoles(
                        initNode.hasParent() ?
                                initNode.getParent().getAvailableForRoles() :
                                Role.getAllRoleSet());
            }
        }
    }

    private void setControllerUrisDefaultsIfAbsentForAllTreeNode(Map<String, MenuInitValueNode> cache) {
        for (Map.Entry<String, MenuInitValueNode> entry : cache.entrySet()) {
            MenuInitValueNode initNode = entry.getValue();
            if (initNode.getControllerUri() == null && !initNode.hasAnyChild()) {
                initNode.setControllerUri('/' + initNode.getReference());
            }
        }
    }

    private void addToMenuInitTree(Map<String, MenuInitValueNode> cache, MenuNode menuNode, MenuInitValueNode initNode) {
        if (menuNode.getParent() == null) {
            rootLevelNodes.add(initNode);
        } else {
            MenuInitValueNode parent = cache.get(menuNode.getParent().getReference());
            initNode.setParent(parent);
            parent.addChildNode(initNode);
        }
    }

    @NotNull
    @Override
    public Iterator<MenuInitValueNode> iterator() {
        return new MenuInitTreeIterator();
    }

    @Override
    public void forEach(Consumer<? super MenuInitValueNode> action) {

    }

    private class MenuInitTreeIterator implements Iterator<MenuInitValueNode> {

        private final Iterator<MenuInitValueNode> rootIterator = MenuInitTree.this.rootLevelNodes.iterator();
        private MenuInitValueNode currentNode = null;

        @Override
        public boolean hasNext() {
            if (currentNode == null && rootIterator.hasNext()) return true;
            if (currentNode.hasAnyChild()) return true;
            if (currentNode.hasParent() && currentNode.getParent().hasNextChild(currentNode)) return true;
            if (rootIterator.hasNext()) return true;
            return false;
        }

        @Override
        public MenuInitValueNode next() {
            MenuInitValueNode newNode = null;
            if (currentNode == null && rootIterator.hasNext()) {
                newNode = rootIterator.next();
            } else if (currentNode.hasAnyChild()) {
                newNode = currentNode.getFirstChild();
            } else if (currentNode.hasParent() && currentNode.getParent().hasNextChild(currentNode)) {
                newNode =  currentNode.getParent().getNextChild(currentNode);
            } else if (rootIterator.hasNext()) {
                newNode = rootIterator.next();
            }
            currentNode = newNode;
            return newNode;
        }
    }

}
