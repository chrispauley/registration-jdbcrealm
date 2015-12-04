package com.simllc.registration.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simllc.registration.dao.UserDao;
import com.simllc.registration.dao.mysql.UserDaoImpl;
import com.simllc.registration.model.User;
import com.simllc.registration.resource.BCrypt;
import com.simllc.registration.utils.CookieBaker;
import com.simllc.registration.model.LoginToken;

public class Signin extends BaseServlet {
	private static final long serialVersionUID = 1L;

	UserDao _dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Signin() {
		super();
		Connection con = null;
		try {
			con = this.getDS().getConnection();
			_dao = new UserDaoImpl(con);
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
		String queryParam = "";
		if (email != null) {
			queryParam = "?email=" + email;
		}
		Cookie cookies[] = request.getCookies();
		if ((cookies == null) || (cookies.length == 0)) {
			System.out.println("No cookies found.");
		} else {
			System.out.println("Cookies found: ");
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				System.out.println("Name " + c.getName() + " " + c.getValue()
						+ " " + c.getComment() + " " + c.getMaxAge() + "\n");
				if (email == null) {
					if (c.getName().equalsIgnoreCase("DefaultUserEmail")) {
						email = c.getValue();
						queryParam = "?email=" + email;
					}
				}
			}
		}
		request.getSession().setAttribute("errorsList", null);
//		LoginToken loginToken = CookieBaker.getCookie(request, LoginToken.class, "hmacCookie");
//		System.out.println("LoginToken: " + loginToken.getUserid() + " " + loginToken.getToken());
		response.sendRedirect(request.getContextPath() + "/account/signin.jsp"
				+ queryParam);
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
		boolean rememberMe = request.getParameter("rememberme") != null;
		Long loginAttemptCount = (Long) request.getSession().getAttribute("loginAttemptCount");
		if(loginAttemptCount==null){
			loginAttemptCount = new Long(0);
		}
		String message = "hello message...";
		request.getSession().setAttribute("errorsList", null);
		request.getSession().setAttribute("message", message);
		String contextPath = request.getContextPath();
		if (email == null || email.length() == 0) {
			errorsList.add("Missing email");
			request.getSession().setAttribute("errorsList", errorsList);
			response.sendRedirect(request.getContextPath() + "/account/signin.jsp");
		}

		System.out.println("email: " + email);
		request.getSession().setAttribute("email", "email");

		User user = _dao.findByEmail(email);
		if (user == null) {
			loginAttemptCount +=1;
			request.getSession().setAttribute("loginAttemptCount", loginAttemptCount);
			errorsList.add("User email / password not found valid.");
			request.getSession().setAttribute("errorsList", errorsList);
			response.sendRedirect(contextPath + "/account/signin.jsp?email="+email);
		} else {

			if (!BCrypt.checkpw(password, user.getPassword())) {
				errorsList.add("User email / password not found.");
				request.getSession().setAttribute("errorsList", errorsList);
				loginAttemptCount +=1;
				request.getSession().setAttribute("loginAttemptCount", loginAttemptCount);
				response.sendRedirect(contextPath + "/account/signin.jsp?email="+email);
			} else {
				_dao.updateLastLoginDate(user.getUserid());
				request.getSession().setAttribute("LoggedInUser", email);
				request.getSession().setAttribute("loginAttemptCount", 0);
				if (rememberMe) {
//					String sessionId = request.getSession().getId();
//					Cookie userCookie = new Cookie("LoginCookie", sessionId);
//					userCookie.setMaxAge(60 * 60 * 24 * 14); // 14 days
//					userCookie.setDomain(request.getLocalName());
//					userCookie.setPath("/");
//					response.addCookie(userCookie);
					//
					Cookie defaultEmail = new Cookie("DefaultUserEmail", email);
					defaultEmail.setPath("/");
					response.addCookie(defaultEmail);
					
					
				}
				// update userlogin
				System.out.println("login success for email: " + email);
				response.sendRedirect(contextPath + "/account/success.jsp");
			}
		}
	}

}
