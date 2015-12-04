package com.simllc.registration.security;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.ext.Provider;

import com.simllc.registration.dao.RoleDao;
import com.simllc.registration.dao.SessionDao;
import com.simllc.registration.dao.UserDao;
import com.simllc.registration.dao.mysql.RoleDaoImpl;
import com.simllc.registration.dao.mysql.SessionDaoImpl;
import com.simllc.registration.dao.mysql.UserDaoImpl;
import com.simllc.registration.security.UserPrincipal;
import com.simllc.registration.model.*;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Filter all incoming requests, look for possible session information and use that
 * to create and load a SecurityContext to request. 
 * @author "Animesh Kumar <animesh@strumsoft.com>"
 * 
 */

//@Component   // let spring manage the lifecycle
@Provider    // register as jersey's provider
public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {

	private final static String DEFAULT_DATASOURCE_NAME = "jdbc/gpxdb";
	private String _datasourceName;
	private DataSource _dataSource;
	UserDao _dao;
	RoleDao roleDao;
	
//	@Autowired
//	private SessionRepository sessionRepository;  // DAO to access Session
//
//	@Autowired
//	private UserRepository userRepository;  // DAO to access User

	
	public SecurityContextFilter() {
		super();
		System.out.println("SecurityContextFilter.constructor");
	}

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		// Get session id from request header
		final String sessionId = request.getHeaderValue("session-id");
		
		UserPrincipal user = null;
		Session session = null;

		if (sessionId != null && sessionId.length() > 0) {
			// Load session object from repository
//			session = sessionRepository.findOne(sessionId);
			session = this.getSession(sessionId);
			
			// Load associated user from session
			if (null != session) {
//				user = userRepository.findOne(session.getUserId());
				user = this.getUser(session.getUserId());
			}
		}

		// Set security context
		request.setSecurityContext(new MySecurityContext(session, user));
		return request;
	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}
	
	protected DataSource getDS() {
		if (_dataSource == null) {
			if (this._datasourceName == null) {
				// Lookup from context, or use the default.
				this._datasourceName = DEFAULT_DATASOURCE_NAME;
			}

			try {
				Context env = (Context) new InitialContext()
						.lookup("java:comp/env");
				if (env != null) {
					_dataSource = (DataSource) env.lookup(_datasourceName);
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return _dataSource;
	}

	protected Connection getConnection() throws SQLException {
		if(_dataSource==null){
			_dataSource = this.getDS();
		}
		return _dataSource.getConnection();
	}
	
	protected UserPrincipal getUser(long userid){
		UserPrincipal user = null;
		com.simllc.registration.model.User appUser = null;
		try {
			Connection con = this.getConnection();
			_dao = new UserDaoImpl(con);
			appUser = _dao.findById(userid);
			user = new com.simllc.registration.security.UserPrincipal();
			user.setEmailAddress(appUser.getEmail());
			user.setName(appUser.getEmail());

			ArrayList<Role> roles = this.getUserRoles(userid);
			for(Role role : roles)
		    {   
		       user.getRoles().add(role.getName());
		    }
			
			
		} catch (SQLException e) {
			System.out.println("SQL Connection Errors!");
			e.printStackTrace();
		}
		return user;
	}
	
	protected Session getSession(String sessionId){
		Session session = null;
		SessionDao dao = null;
		try {
			Connection con = this.getConnection();
			dao = new SessionDaoImpl(con);
			session = dao.findSession(sessionId);
			
		} catch (SQLException e) {
			System.out.println("SQL Connection Errors!");
			e.printStackTrace();
		}		
		return session;
	}
	
	protected ArrayList<Role> getUserRoles(long userid){
		ArrayList<Role> roles = null;
		RoleDao dao = null;
		try {
			Connection con = this.getConnection();
			dao = new RoleDaoImpl(con);
			roles = dao.findRolesByUserId(userid);
			
		} catch (SQLException e) {
			System.out.println("SQL Connection Errors!");
			e.printStackTrace();
		}
		return roles;
	}


}