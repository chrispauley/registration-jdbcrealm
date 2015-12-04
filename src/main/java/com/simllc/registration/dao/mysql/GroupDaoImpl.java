package com.simllc.registration.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.simllc.registration.dao.*;
import com.simllc.registration.model.Group;
import com.simllc.registration.model.Role;

public class GroupDaoImpl implements GroupDao {

	private Connection connection = null;

	public GroupDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	public void delete(long groupid) {
		try {
			String sql = "delete from user_group where groupid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, groupid);
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Group findByName(String groupname) {
		Group group = null;
		if (groupname == null) {
			return null;
		}
		ResultSet rs = null;
		try {
			String sql = "Select * from user_group where name=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, groupname);
			rs = ps.executeQuery();
			while (rs.next()) {
				group = getGroupFromResultset(rs);
			}
			rs.close();
			ps.close();
//			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return group;
	}
	
	/**
	 * 
	 */
	public ArrayList<Group> findGroupsByEmail(String email){
		ArrayList<Group> groups = new ArrayList<Group>();
		ResultSet rs = null;
		try {
			String sql = "Select  a.groupid, a.name, a.description "
	        + "FROM user_group a, user_group_members b, user c "
	        + "WHERE a.groupid = b.groupid AND b.userid=c.userid "
	        + "AND c.email=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			while (rs.next()) {
				groups.add(getGroupFromResultset(rs));
			}
			rs.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			DaoException d = new DaoException("DataExceoption", e);
			d.printStackTrace();
		}
		return groups;	
	}
	

	public Group findById(long id) {
		Group group = null;
		ResultSet rs = null;
		try {
			String sql = "Select * from user_group where groupid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				group = getGroupFromResultset(rs);
			}
			rs.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return group;
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
		connection.close();
		return result;
	}

	public long getTotalCount() {
		long totalcount = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT count(groupid) AS totalcount FROM user_group");
			while (rs.next()) {
				totalcount = rs.getLong("totalcount");
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	private java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

	private Group getGroupFromResultset(ResultSet rs) throws SQLException {
		Group group = new Group();
		group.setGroupid(rs.getLong("groupid"));
		group.setName(rs.getString("name"));
		group.setDescription(rs.getString("description"));
		return group;
	}

	public long insert(Group group){
		long result = 0;
		try {
			String sql = "INSERT INTO user_group "
					+ "(name,description,created) "
					+ "VALUES (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, group.getName());
			ps.setString(2, group.getDescription());
			ps.setTimestamp(3, this.getCurrentTimeStamp());
			ps.executeUpdate();
			ps.close();
			result = this.getLastUpdateId();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Group> listAll() {
		ArrayList<Group> groupList = new ArrayList<Group>();
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM user_group ";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				groupList.add(this.getGroupFromResultset(rs));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupList;
	}


	public void update(Group group) {
		try {
			String sql = "UPDATE user_group " + "SET name=?, description=? "
					+ "WHERE groupid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, group.getName());
			ps.setString(2, group.getDescription());
			ps.setLong(3, group.getGroupid());
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
