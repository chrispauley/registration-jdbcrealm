package com.simllc.registration.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.simllc.registration.dao.RoleMembersDao;
import com.simllc.registration.model.*;

public class RoleMembersDaoImpl implements RoleMembersDao {

	private Connection connection = null;

	public RoleMembersDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}


	public void delete(long roleid, long userid) {
		try {
			String sql = "delete from user_role_members where roleid=? "
					+ " and userid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, roleid);
			ps.setLong(2, userid);
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public GroupMember findById(long id) {
		GroupMember groupMember = null;
		ResultSet rs = null;
		try {
			String sql = "select * from user_role_members where groupid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				groupMember = getGroupMembersFromResultset(rs);
			}
			rs.close();
			ps.close();
			connection.close();
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
					.executeQuery("SELECT count(groupid) AS totalcount FROM user_role_members");
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
		
		return groupmember;
	}

	public long insert(long groupid, long userid) {
		long result = 0;
		try {
			String sql = "INSERT INTO user_role_members "
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

	public ArrayList<GroupMember> listGroupMembersByRole(long roleid) {
		ArrayList<GroupMember> groupList = new ArrayList<GroupMember>();
		try {
			String sql = "SELECT a.rmid , a.roleid, c.name role_name,"
				+ "c.description role_description , a.groupid, b.name group_name,"
				+ "b.description group_description "
				+ "FROM  user_role_members a, user_group b, user_role c "
				+ "WHERE a.groupid = b.groupid AND a.roleid = c.roleid "
                + " ORDER BY a.roleid AND a.roleid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, roleid);
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

	public ArrayList<Role> listRolesByGroup(long groupid) {
		ArrayList<Role> roleList = new ArrayList<Role>();
		try {
			String sql = "SELECT * FROM user_role_members "
					+ "WHERE groupid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, groupid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				roleList.add(this.getRoleFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roleList;
	}

	private Role getRoleFromResultset(ResultSet rs) throws SQLException {
		Role role = new Role();
		role.setRoleid(rs.getLong("roleid"));
		role.setName(rs.getString("name"));
		// user.setEmail(rs.getString("email"));
		return role;
	}

}
