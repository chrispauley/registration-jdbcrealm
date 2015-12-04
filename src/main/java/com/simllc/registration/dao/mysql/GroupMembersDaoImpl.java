package com.simllc.registration.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.simllc.registration.dao.GroupMembersDao;
import com.simllc.registration.model.GroupMember;
import com.simllc.registration.model.User;

public class GroupMembersDaoImpl implements GroupMembersDao {
	// 'SELECT u.username, ug.name FROM `user` `u`,
	// `user_group` `ug`, `user_group_members` `ugm` WHERE u.userid=ugm.userid
	// and ug.groupid=ugm.groupid;'

	private Connection connection = null;

	public GroupMembersDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simllc.registration.dao.GroupMembersDao#delete(long, long)
	 */
	public void delete(long groupid, long userid) {
		try {
			String sql = "delete from user_group_members where groupid=? "
					+ " and userid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, groupid);
			ps.setLong(2, userid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public GroupMember findById(long id) {
		GroupMember groupMember = null;
		ResultSet rs = null;
		try {
			String sql = "select * from user_group_members where groupid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				groupMember = getGroupMembersFromResultset(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupMember;
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
					.executeQuery("SELECT count(groupid) AS totalcount FROM user_group_members");
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

	private GroupMember getGroupMembersFromResultset(ResultSet rs)
			throws SQLException {
		GroupMember groupmember = new GroupMember();
		groupmember.setGroupid(rs.getLong("groupid"));
		groupmember.setUserid(rs.getLong("userid"));
		return groupmember;
	}

	public long insert(long groupid, long userid) {
		long result = 0;
		try {
			String sql = "INSERT INTO user_group_members "
					+ "(groupid, userid) " + "VALUES (?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, groupid);
			ps.setLong(2, userid);
			ps.executeUpdate();
			ps.close();
			result = this.getLastUpdateId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<GroupMember> listGroupMembersByUserId(long userid) {
		ArrayList<GroupMember> groupList = new ArrayList<GroupMember>();
		try {
			String sql = "SELECT * FROM user_group_members " + "WHERE userid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, userid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				groupList.add(this.getGroupMembersFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupList;
	}

	public ArrayList<User> listUsersByGroupId(long groupid) {
		ArrayList<User> userList = new ArrayList<User>();
		try {
			String sql = "SELECT * FROM user_group_members "
					+ "WHERE groupid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, groupid);
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

	private User getUserFromResultset(ResultSet rs) throws SQLException {
		User user = new User();
		user.setUserid(rs.getLong("userid"));
		// user.setEmail(rs.getString("email"));
		return user;
	}

}
