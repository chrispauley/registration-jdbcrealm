package com.simllc.registration.security;

import java.util.HashSet;
import java.util.Set;


public class UserPrincipal implements java.security.Principal {

	private String userId;          // id
	private String name;            // name
	private String emailAddress;    // email
	private Set<String> roles;        // roles

	@Override
	public String getName() {
		return null;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Set<String> getRoles() {
		if(null==roles){
			roles = new HashSet<String>();
		}
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void setName(String name) {
		this.name = name;
	}

}