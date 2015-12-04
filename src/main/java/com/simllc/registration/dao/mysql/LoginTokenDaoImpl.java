package com.simllc.registration.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.datatype.XMLGregorianCalendar;
import com.simllc.registration.utils.*;

import com.simllc.registration.dao.LoginTokenDao;
import com.simllc.registration.model.LoginToken;

public class LoginTokenDaoImpl implements LoginTokenDao {

	private Connection connection = null;

	public LoginTokenDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	public void delete(long id) {
		try {
			String sql = "delete from user_login_token where id=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, id);
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isValidToken(LoginToken loginToken) {
		boolean result = false;
		ResultSet rs = null;
		try {
			String sql = "Select * "
					+ "FROM user_login_token "
					+ "WHERE token=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, loginToken.getToken());
			rs = ps.executeQuery();
			while (rs.next()) {
				//System.out.println("Found user " +email +" in role:" + rolename);
				result = true;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Wrap into DaoException()
			e.printStackTrace();
		}

		return result;
	}

	public LoginToken findByToken(String token) {
		LoginToken loginToken = null;
		if (token == null) {
			return null;
		}
		ResultSet rs = null;
		try {
			String sql = "Select * FROM user_login_token "
				+ "WHERE token=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, token);
			rs = ps.executeQuery();
			while (rs.next()) {
				loginToken = getLoginTokenFromResultset(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginToken;
	}

	public LoginToken findById(long id) {
		LoginToken loginToken = null;
		ResultSet rs = null;
		try {
			String sql = "Select * from user_login_token where id=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				loginToken = getLoginTokenFromResultset(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginToken;
	}

	public ArrayList<LoginToken> findTokensByUserEmail(String email) {
		ArrayList<LoginToken> loginTokens = new ArrayList<LoginToken>();
		ResultSet rs = null;
		try {
			String sql = "Select a.id, a.token, a.userid, set_time "
					+ "FROM user_login_token a , user b "
					+ "WHERE a.userid in (Select userid "
					+ "FROM user WHERE email=?)";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			while (rs.next()) {
				loginTokens.add(this.getLoginTokenFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Wrap into DaoException()
			e.printStackTrace();
		}
		return loginTokens;
	}

	public ArrayList<LoginToken> findTokensByUserid(Long userid) {
		ArrayList<LoginToken> loginTokens = new ArrayList<LoginToken>();
		ResultSet rs = null;
		try {
			String sql = "Select a.id, a.token, a.userid, set_time "
					+ "FROM user_login_token a , user b "
					+ "WHERE a.userid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				loginTokens.add(this.getLoginTokenFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Wrap into DaoException()
			e.printStackTrace();
		}
		return loginTokens;
	}


	/**
	 * Gets the last update id.
	 * 
	 * @return long LAST_INSERT_ID()
	 * @throws SQLException
	 *             the sQL exception
	 */
	private long getLastUpdateId() throws SQLException {
		long result = 0;
		ResultSet resultset = null;
		Statement statement = this.connection.createStatement();
		resultset = statement.executeQuery("SELECT LAST_INSERT_ID()");
		while (resultset.next()) {
			result = resultset.getInt(1);
		}
		resultset.close();
		statement.close();
		return result;
	}

	public long getTotalCount() {
		long totalcount = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT count(id) AS totalcount FROM user_login_token");
			while (rs.next()) {
				totalcount = rs.getLong("totalcount");
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	private java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

	private LoginToken getLoginTokenFromResultset(ResultSet rs) throws SQLException {
		LoginToken loginToken = new LoginToken();
		loginToken.setId(rs.getLong("id"));
		loginToken.setToken(rs.getString("token"));
		loginToken.setUserid(rs.getLong("userid"));
		loginToken.setSetTime( XMLGregorianCalendarConverter.asXMLGregorianCalendar( rs.getTime("set_time")));
		return loginToken;
	}

	public LoginToken create(long userid) {
		LoginToken loginToken = null;
		try {
			String sql = "INSERT INTO user_login_token " + "(token,userid,set_time) "
					+ "VALUES (uuid(),?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, userid);
			ps.setTimestamp(2, this.getCurrentTimeStamp());
			ps.executeUpdate();
			ps.close();
			long id =this.getLastUpdateId();
			loginToken = this.findById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginToken;
	}
	
	public long insert(LoginToken loginToken ) {
		long result = 0;
		try {
			String sql = "INSERT INTO user_login_token " + "(token,userid,set_time) "
					+ "VALUES (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, loginToken.getToken());
			ps.setLong(2, loginToken.getUserid());
			ps.setTimestamp(3, this.getCurrentTimeStamp());
			ps.executeUpdate();
			ps.close();
			result = this.getLastUpdateId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<LoginToken> listAll() {
		ArrayList<LoginToken> loginTokens = new ArrayList<LoginToken>();
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM user_login_token ";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				loginTokens.add(getLoginTokenFromResultset(rs));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginTokens;
	}

}
