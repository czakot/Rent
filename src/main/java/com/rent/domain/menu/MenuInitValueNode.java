package com.rent.domain.menu;

import com.rent.domain.Role;

import java.util.*;

public class MenuInitValueNode {

    private String reference;
    private String controllerUri;
    private MenuInitValueNode parent;
    private Set<Role> availableForRoles;
    private List<MenuInitValueNode> childNodeList;


    public MenuInitValueNode(String reference, String controllerUri) {
        this.reference = reference;
        this.controllerUri = controllerUri;
        this.parent = null;
        availableForRoles = null;
        childNodeList = new ArrayList<>();
    }

    public void addChildNode(MenuInitValueNode newChild) {
        childNodeList.add(newChild);
    }

    public boolean hasNextChild(MenuInitValueNode child) {
        int index = childNodeList.indexOf(child);
        if (index == -1) {
            throw new NoSuchElementException("'child' parameter is not element of childNodeList");
        }
        return index < childNodeList.size() - 1;
    }

    public MenuInitValueNode getNextChild(MenuInitValueNode child) {
        int index = childNodeList.indexOf(child);
        if (index == -1 || index == childNodeList.size() - 1) {
            throw new NoSuchElementException("'child' parameter is last element or not element of childNodeList");
        }
        return childNodeList.get(index + 1);
    }

    public MenuInitValueNode getFirstChild() {
        return childNodeList.get(0);
    }

    public boolean hasAnyChild() {
        return childNodeList != null && !childNodeList.isEmpty();
    }

    public String getReference() {
        return reference;
    }

    public String getControllerUri() {
        return controllerUri;
    }

    public void setControllerUri(String controllerUri) {
        this.controllerUri = controllerUri;
    }

    public MenuInitValueNode getParent() {
        return parent;
    }

    public void setParent(MenuInitValueNode parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Set<Role> getAvailableForRoles() {
        return availableForRoles;
    }

    public void setAvailableForRoles(Set<Role> availableForRoles) {
        this.availableForRoles = availableForRoles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                "MenuInitValueNode{" +
                "reference='" + reference + '\'' +
                ", controllerUri='" + controllerUri + '\'' +
                ", parent=" + (parent == null ? parent : parent.getReference()) +
                ", availableForRoles=" + availableForRoles +
                ", childNodeList=["
        );
        if (childNodeList.size() != 0) {
            for (MenuInitValueNode menuInitValueNode : childNodeList) {
                sb.append(menuInitValueNode.getReference()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append('}');
        return sb.toString();
    }
}
