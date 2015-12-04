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

public class Signout extends BaseServlet {
	private static final long serialVersionUID = 1L;

	UserDao _dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Signout() {
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
		String email = (String) request.getSession().getAttribute("LoggedInUser");
		User user = _dao.findByEmail(email);
		if(user!=null){
			_dao.updateLastLoginDate(user.getUserid());
			System.out.println(user.getUserid() + " " + user.getEmail());
		}
		request.getSession().invalidate();
		request.setAttribute("message", "Logout was successful.");
		String contextPath = request.getContextPath();
		response.sendRedirect(contextPath +"/");
	}


}
