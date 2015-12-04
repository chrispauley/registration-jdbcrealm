package com.simllc.registration.security;


import java.io.Serializable;
import java.util.Date;


public class Session implements Serializable {
	// 
	private static final long serialVersionUID = 7526471155622776147L;
	
	private String sessionId;   // id
	private Long userId;      // user
	private boolean active = false;     // session active?
	private boolean secure = false;     // session secure?

	private Date createTime;    // session create time
	private Date lastAccessedTime;  // session last use time
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isSecure() {
		return secure;
	}
	public void setSecure(boolean secure) {
		this.secure = secure;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastAccessedTime() {
		return lastAccessedTime;
	}
	public void setLastAccessedTime(Date lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	// getters/setters here
}
