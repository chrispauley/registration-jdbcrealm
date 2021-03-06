package com.simllc.registration.web;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

public abstract class BaseServlet extends HttpServlet {
	//
	private final static String DEFAULT_DATASOURCE_NAME = "jdbc/gpxdb";
	private String _datasourceName;
	private DataSource _dataSource;

	/**
	 * @return DataSource
	 */
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

}
