package com.simllc.registration.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simllc.registration.dao.UserDao;
import com.simllc.registration.dao.mysql.UserDaoImpl;
import com.simllc.registration.dao.GroupDao;
import com.simllc.registration.dao.mysql.GroupDaoImpl;
import com.simllc.registration.dao.GroupMembersDao;
import com.simllc.registration.dao.LoginTokenDao;
import com.simllc.registration.dao.mysql.LoginTokenDaoImpl;
import com.simllc.registration.dao.mysql.GroupMembersDaoImpl;
import com.simllc.registration.mail.EmailValidator;
import com.simllc.registration.mail.Notifier;
import com.simllc.registration.model.Group;
import com.simllc.registration.model.LoginToken;
import com.simllc.registration.model.User;
import com.simllc.registration.resource.BCrypt;
import com.simllc.registration.utils.*;

/**
 * Servlet implementation class Signup
 * 
 * 1. If GET /signup forward to signup.jsp
 * 
 * 1. POST /signup 1.1 If email is invalid, update the signup.jsp 1.2 If valid
 * 1.2.1 Add row to user table with an activation token. 1.2.2 Send an html
 * email 'thanks for signingup' with activation link.
 * 
 */
public class Activate extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private UserDao _dao;
	private GroupMembersDao _groupMembersDao;
	private GroupDao _groupDao;
	private LoginTokenDao _loginTokenDao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Activate() {
		super();
		Connection con = null;
		try {
			con = this.getDS().getConnection();
			_dao = new UserDaoImpl(con);
			_groupMembersDao = new GroupMembersDaoImpl(con);
			_groupDao = new GroupDaoImpl(con);
			_loginTokenDao = new LoginTokenDaoImpl(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String auth = request.getParameter("auth");
        
		ArrayList<String> errorsList = new ArrayList<String>();
		request.getSession().setAttribute("email", email);
		request.getSession().setAttribute("activateToken", auth);
		String contextPath = request.getContextPath();
		User user = _dao.findByToken(auth);
		
		if (user != null) {
			System.out.println("found user for: " + auth);
			System.out.println("Welcome new user: " + user.getEmail());
			
			if(email != null && email.equalsIgnoreCase(user.getEmail())){
				_dao.activateUser(user.getUserid());
				request.getSession().setAttribute("message",
						"Welcome new user: " + user.getEmail());
				request.getSession().setAttribute("activateToken", auth);
				response.sendRedirect(contextPath + "/account/activate.jsp?email=" + user.getEmail());
			} else {
				// Set error message, log it, and redirect to signup.
				errorsList.add("The email provided does not match the authorization token");
				response.sendRedirect(contextPath + "/account/signup.jsp?email="+email);
			}
			
		} else {
			// User account not found for the authorization token.
			errorsList.add("The email provided does not match the authorization token");
			request.getSession().setAttribute("errorsList", errorsList);
			response.sendRedirect(contextPath + "/account/activate.jsp?email="+email);
		}

	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList<String> errorsList = new ArrayList<String>();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirm_password = request.getParameter("confirm_password");
		String contextPath = request.getContextPath();
		if (email == null || email.length() == 0) {
			errorsList.add("Missing email");
			request.setAttribute("errorsList", errorsList);
			response.sendRedirect(contextPath + "/account/signup.jsp");
		}
		ArrayList<String> errorList = new ArrayList<String>();
		User user = _dao.findByEmail(email);
		if (user == null) {
			errorList.add("User not found by email");
			request.setAttribute("errorList", errorList);
			response.sendRedirect(contextPath + "/account/activate.jsp");
		} else {
			if (!password.equals(confirm_password)) {
				errorList.add("Password and confirmed password do not match.");
			} else {
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
				_dao.updatePassword(user.getUserid(), hashedPassword);
				_dao.activateUser(user.getUserid());
				_dao.updateSharedSecret(user.getUserid());
				if(!addToRegisteredUsersGroup(user.getUserid())){
					errorList.add("Could not add user to group");
				}
				
				LoginToken loginToken =  _loginTokenDao.create(user.getUserid());
//				CookieBaker.saveCookie(response, "hmacCookie", loginToken, "/", 320000, request.getLocalName());
				response.sendRedirect(contextPath + "/account/signin.jsp?email="+user.getEmail());
			}
		}
	}
	
	private boolean addToRegisteredUsersGroup(long userid){
		boolean result = false;
		Group group = this._groupDao.findByName("registered_user");
		if (group!=null){
			_groupMembersDao.insert(group.getGroupid(), userid);
		}
		return result;
	}

}
