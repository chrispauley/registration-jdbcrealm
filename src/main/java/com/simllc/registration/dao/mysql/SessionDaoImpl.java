package com.simllc.registration.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.simllc.registration.dao.SessionDao;
import com.simllc.registration.security.Session;

public class SessionDaoImpl implements SessionDao {

	private Connection connection = null;

	public SessionDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public Session findSession(String sessionId) {
		Session session = null;
		try {
			String sql = "Select * from user_session where " + " sessionid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, sessionId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				session = getSessionFromResultset(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return session;
	}

	/*
	 * 
	 * 
	 */
	private Session getSessionFromResultset(ResultSet rs) throws SQLException {
		Session session = new Session();
		session.setUserId(rs.getLong("userid"));
		session.setSessionId(rs.getString("sessionid"));
		if (rs.getInt("active") > 0) {
			session.setActive(true);
		}
		if (rs.getInt("secure") > 0) {
			session.setSecure(true);
		}
//		session.setCreateTime(rs.getTimestamp("create_time"));
//		session.setLastAccessedTime(rs.getTime("last_accessed"));
		return session;
	}

	@Override
	public String saveSession(Session session) {
		String result = null;
		try {
			String sql = "INSERT INTO user_session "
					+ "(userid,sessionid,active,secure,create_time,last_active) "
					+ "VALUES (?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, session.getUserId());
			ps.setString(2, session.getSessionId());
			if (session.isActive()) {
				ps.setInt(3, 1);
			} else {
				ps.setInt(3, 0);
			}
			if (session.isSecure()) {
				ps.setInt(4, 1);
			} else {
				ps.setInt(4, 0);
			}
			ps.setTimestamp(5, this.getCurrentTimeStamp());
			ps.setTimestamp(6, this.getCurrentTimeStamp());
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String updateSession(Session session) {
		String result = null;
//		try {
//			String sql = "Update user_session "
//					+ "SET userid,sessionid,active,secure,create_time,last_active) "
//					+ "VALUES (?,?,?,?,?,?)";
//			PreparedStatement ps = connection.prepareStatement(sql);
//			ps.setLong(1, session.getUserId());
//			ps.setString(2, session.getSessionId());
//			if (session.isActive()) {
//				ps.setInt(3, 1);
//			} else {
//				ps.setInt(3, 0);
//			}
//			if (session.isSecure()) {
//				ps.setInt(4, 1);
//			} else {
//				ps.setInt(4, 0);
//			}
//			ps.setTimestamp(5, this.getCurrentTimeStamp());
//			ps.setTimestamp(6, this.getCurrentTimeStamp());
//			ps.executeUpdate();
//			ps.close();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return result;
	}

	private java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

}
