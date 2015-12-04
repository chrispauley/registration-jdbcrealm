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
import com.simllc.registration.mail.EmailValidator;
import com.simllc.registration.mail.Notifier;
import com.simllc.registration.model.User;
import com.simllc.registration.resource.BCrypt;

/**
 * Servlet implementation class Signup
 * 
 * 1. If GET /reset forward to forgotPassword.jsp
 * 
 * 1. POST /reset 1.1 If email is invalid, update the forgotPassword.jsp 
 * 1.2 If valid
 * 1.2.1 Update the user table with an activation token. 
 * 1.2.2 Send an html
 * email 'Here is your reset link' with reset link.
 * 
 */
public class Reset extends BaseServlet {
	private static final long serialVersionUID = 1L;

	UserDao _dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Reset() {
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
		request.getSession().setAttribute("errorsList", null);
		String token = request.getParameter("token");
		System.out.println("GET account/reset?token= " + token);
		User user = _dao.findByToken(token);
		if (user != null) {
			System.out.println("Found user for: " + token + "\nWelcome returning user: " + user.getEmail());
			_dao.activateUser(user.getUserid());
			request.getSession().setAttribute("message",
					"Welcome back user: " + user.getEmail());
			response.sendRedirect(request.getContextPath() + "/account/forgotPassword.jsp");
		} else {
			// 1. User never existed.
			// 2. Token expired, user exists.
			request.setAttribute("message", "Email account for the given token was not found");
			response.sendRedirect(request.getContextPath() + "/account/signup");
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
		if (email == null || email.length() == 0) {
			errorsList.add("Missing email");
			request.setAttribute("errorsList", errorsList);
			response.sendRedirect("signup.jsp");
		}
		ArrayList<String> errorList = new ArrayList<String>();
		User user = _dao.findByEmail(email);
		if (user == null) {
			errorList.add("User not found by email");
			request.setAttribute("errorList", errorList);
			response.sendRedirect("activate.jsp");
		} else {
			if (!password.equals(confirm_password)) {
				errorList.add("Password and confirmed password do not match.");
			} else {
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
				_dao.updatePassword(user.getUserid(), hashedPassword);
				response.sendRedirect("login.jsp");
			}
		}
	}

}
