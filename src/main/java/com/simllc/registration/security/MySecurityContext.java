package com.simllc.registration.security;

import java.security.Principal;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public class MySecurityContext implements javax.ws.rs.core.SecurityContext {

	private final UserPrincipal user;
	private final Session session;

	public MySecurityContext(Session session, UserPrincipal user) {
		this.session = session;
		this.user = user;
		System.out.println("MySecurityContext.constructor called.");
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return user;
	}

	@Override
	public boolean isSecure() {
		return (null != session) ? session.isSecure() : false;
	}

	@Override
	public boolean isUserInRole(String role) {

		if (null == session || !session.isActive()) {
			// Forbidden
			Response denied = Response.status(Response.Status.FORBIDDEN).entity("Permission Denied").build();
			throw new WebApplicationException(denied);
		}

		try {
			// this user has this role?
//			return user.getRoles().contains(UserPrincipal.Role.valueOf(role));
			return user.getRoles().contains(role);
		} catch (Exception e) {
		}
		
		return false;
	}
}