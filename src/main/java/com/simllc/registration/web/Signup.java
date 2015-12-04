package com.simllc.registration.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.simllc.registration.dao.UserDao;
import com.simllc.registration.dao.mysql.UserDaoImpl;
import com.simllc.registration.mail.EmailValidator;
import com.simllc.registration.mail.Notifier;
import com.simllc.registration.model.User;
import com.simllc.registration.resource.BCrypt;

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
public class Signup extends BaseServlet {
	private static final long serialVersionUID = 1L;

	UserDao _dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Signup() {
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
		String contextPath = request.getContextPath();
		request.getSession().setAttribute("errorsList", null);
		if (email == null || email.length() == 0) {
			response.sendRedirect(contextPath + "/account/signup.jsp");
		} else {
			response.sendRedirect(contextPath + "/account/signup.jsp?email="+email);
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
		String contextPath = request.getContextPath();
		if (email == null || email.length() == 0) {
			errorsList.add("Missing email");
			request.getSession().setAttribute("errorsList", errorsList);
			response.sendRedirect(contextPath + "/account/signup.jsp");
		}
		EmailValidator emailValidator = new EmailValidator();
		if (!emailValidator.validate(email)) {
			errorsList.add("Invalid email -->" + email);
			request.getSession().setAttribute("errorsList", errorsList);
			response.sendRedirect(contextPath + "/account/signup.jsp");
		}
		User user = _dao.findByEmail(email);
		if (user != null) {
			errorsList.add("Email account exists");
			request.getSession().setAttribute("errorsList", errorsList);
			String errors = new ObjectMapper().writeValueAsString(errorsList);
			request.getSession().setAttribute("errorsJson", errors);
			response.sendRedirect(contextPath + "/account/signup.jsp?email="+email);
		} else {
			String hashed = BCrypt.hashpw(email + "drowssap", BCrypt.gensalt());
			String baseUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ contextPath;
			
			String activateUrl = "";
			try {
				URI uri = new URI(
						request.getScheme(), 
						request.getServerName() + ":" + request.getServerPort()
						+ contextPath, 
				        "/account/activate?",
				        URLEncoder.encode("email=" + email + ";auth="+hashed, "UTF-8"),
				        null);
				activateUrl = uri.toASCIIString();
				System.out.println("ActivateUrl: " + activateUrl);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			 

			String activationUrl = baseUrl + "/account/activate?"
			+ "email=" + email.toLowerCase() + "&amp;auth=" + hashed;
			System.out.println("activationUrl: " + activateUrl);
			user = new User();
			user.setEmail(email.toLowerCase());
			user.setPassword("");
			user.setTempPass(hashed);
			user.setTempPassActive(1);
			user.setLevelAccess("0");
			long userid = this._dao.insert(user);
			user.setUserid(userid);

			Notifier notifier = new Notifier();
			String result = notifier.sendActivationMessage(user, activationUrl);
			if (!result.contains(result)) {
				errorsList.add("Problem occurred sending an activation url to "
						+ email);
				request.getSession().setAttribute("errorsList", errorsList);
				response.sendRedirect(contextPath + "/account/issues.jsp");
			} else {
				response.sendRedirect(contextPath + "/account/signup_requested.jsp?email="+email);
			}
		}

	}

}
