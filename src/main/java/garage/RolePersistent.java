package garage;

//package com.rent.entity;
//
//import java.util.HashSet;
//import java.util.Objects;
//import java.util.Set;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//
//@Entity
//@Table( name = "roles" )
//public class RolePersistent {
//
//	@Id
//	@GeneratedValue
//	private Long id;
//	
//	private String role;
//	
//	@ManyToMany( mappedBy = "roles")
//	private Set<User> users = new HashSet<User>();
//	
//	public RolePersistent(){} 
//	
//	public RolePersistent(String role){
//		this.role=role;
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getRole() {
//		return role;
//	}
//
//	public void setRole(String role) {
//		this.role = role;
//	}
//
//	public Set<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}
//
//	@Override
//	public String toString() {
//		return role;
//	}
////	@Override
////	public String toString() {
////		return "Role [id=" + id + ", role=" + role + "]";
////	}
//	
//        @Override
//        public boolean equals(Object obj) {
//        // compare by role only (leave out Id)
//            if(obj == this) {
//                return true;
//            }
//            if(obj == null) {
//                return false;
//            }
//            if(getClass() != obj.getClass()) {
//                return false;
//            }
//            RolePersistent r = (RolePersistent)obj;
//            return this.role.equalsIgnoreCase(r.getRole());
//        }
//
//        @Override
//        public int hashCode() {
//            int hash = 7;
//            hash = 29 * hash + Objects.hashCode(this.role);
//            return hash;
//        }
//}
