package com.simllc.registration.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.simllc.registration.dao.UserDao;
import com.simllc.registration.model.Role;
import com.simllc.registration.model.User;

public class UserDaoImpl implements UserDao {

	private Connection connection = null;

	public UserDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public void activateUser(long userid) {
		try {
			String sql = "UPDATE user " + "SET level_access=1, active=1 "
					+ " ,temp_pass=null, temp_pass_active=0 "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deactivateUser(long userid) {
		try {
			String sql = "UPDATE user " + "SET level_access=0, active=0 "
					+ " ,temp_pass=null, temp_pass_active=0 "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void delete(long userid) {
		try {
			String sql = "DELETE FROM user WHERE userid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public User findByEmail(String email) {
		User user = null;
		if (email == null) {
			return null;
		}
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM user WHERE email=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			while (rs.next()) {
				user = getUserFromResultset(rs);
			}
			rs.close();
			ps.close();
			//connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User findById(long userid) {
		User user = null;
		try {
			String sql = "Select * from user " + "where userid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, userid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = getUserFromResultset(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User findByToken(String temp_pass) {
		User user = null;
		try {
			String sql = "select * from user where temp_pass=? limit 1";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, temp_pass);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = getUserFromResultset(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	private java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
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

	public String getSalt(String email) {
		String salt = null;
		try {
			PreparedStatement ps = connection
					.prepareStatement("SELECT salt from user where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				salt = rs.getString(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return salt;
	}

	public long getTotalCount() {
		long totalcount = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT count(userid) AS totalcount FROM user");
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

	private User getUserFromResultset(ResultSet rs) throws SQLException {
		// SELECT `userid`, `email`, `username`, `password`, `temp_pass`,
		// `temp_pass_active`, `active`, `level_access`, `hash`,
		// `salt`, `created_date`, `last_login` FROM `user` WHERE 1
		User user = new User();
		user.setUserid(rs.getLong("userid"));
		user.setEmail(rs.getString("email"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setTempPass(rs.getString("temp_pass"));
		user.setTempPassActive(rs.getInt("temp_pass_active"));
		user.setActive(rs.getInt("active"));
//		user.setLevelAccess(Long(rs.getLong("level_access"));
		return user;
	}

	public long insert(User user) {
		// userid`, `email`, `username`, `password`, `temp_pass`,
		// `temp_pass_active`, `active`, `level_access`, `hash`,
		// `salt`, `created_date`, `last_login`
		long result = 0;
		try {
			String sql = "INSERT INTO user "
					+ "(email,username,password,temp_pass,created_date) "
					+ "VALUES (?,?,?,?,?)"; 
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getTempPass());
			// ps.setBoolean(5, user.getTempPassActive());
			// ps.setBoolean(6, user.getActive());
			// ps.setLong(7, user.getLevelAccess());
			// ps.setString(8, user.getHash());
			// ps.setString(9, user.getSalt());
			ps.setTimestamp(5,this.getCurrentTimeStamp());
			ps.executeUpdate();
			ps.close();
			result = this.getLastUpdateId();
			user.setUserid(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<User> listAll() {
		ArrayList<User> userList = new ArrayList<User>();
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM user ";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				userList.add(this.getUserFromResultset(rs));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public ArrayList<User> listAllPaged(long start, long offset, String orderby) {
		ArrayList<User> userList = new ArrayList<User>();
		if (start < 0)
			start = 0;
		if (offset < 0)
			offset = 1;
		try {
			String sql = "SELECT * FROM user ";
			if (orderby != null && orderby.length() > 0) {
				if (orderby.equalsIgnoreCase("email")) {
					sql += " ORDER BY " + orderby;
				} else if (orderby.equalsIgnoreCase("username")) {
					sql += " ORDER BY " + orderby;
				}
			}
			sql += " LIMIT ?, ? ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, start);
			ps.setLong(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				userList.add(this.getUserFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public void update(User user) {
		try {
			String sql = "UPDATE user " + "SET email=?,username=? "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getUsername());
			ps.setLong(3, user.getUserid());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateLastLoginDate(long userid){
		try {
			String sql = "UPDATE user " + "SET last_login=? "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setTimestamp(1, this.getCurrentTimeStamp());
			ps.setLong(2, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void logout(long userid){
		try {
			String sql = "UPDATE user " + "SET last_login=? "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setTimestamp(1, this.getCurrentTimeStamp());
			ps.setLong(2, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void updatePassword(long userid, String hashedPassword) {
		try {
			String sql = "UPDATE user " + "SET password=? "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, hashedPassword);
			ps.setLong(2, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public String updateSharedSecret(long userid) {
		String result = null;
		try {
			String sql = "UPDATE user " + "SET shared_secret=uuid() "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, userid);
			ps.executeUpdate();
			ps.close();
			String resultSql = "Select shared_secret FROM user "
			+ "WHERE userid=?";
			ps = connection.prepareStatement(resultSql);
			ps.setLong(1, userid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString("shared_secret");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * 
	 */
	public void updateTempPassword(long userid, String hashedPassword) {
		try {
			String sql = "UPDATE user " + "SET temp_pass=? "
					+ "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, hashedPassword);
			ps.setLong(2, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean verifyUser(String email, String password) {
		try {
			String sql = "SELECT email from user where email=? and password=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
