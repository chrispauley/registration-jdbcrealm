package com.simllc.registration.dao;

import com.simllc.registration.security.Session;

public interface SessionDao {

	/**
	 * Used for session authentication.
	 * @param sessionId
	 * @return Session object
	 */
	public Session findSession(String sessionId);
	
	/**
	 * Used for session authentication.
	 * @param Session object
	 * @return String
	 */
	public String saveSession(Session session);
	
	/**
	 * Used for session authentication.
	 * @param Session object
	 * @return String
	 */
	public String updateSession(Session session);
	
}
