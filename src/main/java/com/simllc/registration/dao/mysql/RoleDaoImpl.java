package com.simllc.registration.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.simllc.registration.dao.RoleDao;
import com.simllc.registration.model.Role;

public class RoleDaoImpl implements RoleDao {

	private Connection connection = null;

	public RoleDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	public void delete(long roleid) {
		try {
			String sql = "delete from role where roleid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, roleid);
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isUserInRole(String rolename, String email) {
		boolean result = false;
		if (email == null || rolename == null) {
			return false;
		}
		ResultSet rs = null;
		try {
			String sql = "Select a.roleid, a.name, a.description "
					+ "FROM user_role a, user_role_members b, "
					+ " user_group_members c, user d "
					+ "WHERE a.roleid=b.roleid AND b.groupid=c.groupid "
					+ "AND c.userid=d.userid AND d.email=? AND a.name=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, rolename);
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

	public Role findByName(String rolename) {
		Role role = null;
		if (rolename == null) {
			return null;
		}
		ResultSet rs = null;
		try {
			String sql = "Select * from user_role where name=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, rolename);
			rs = ps.executeQuery();
			while (rs.next()) {
				role = getRoleFromResultset(rs);
			}
			rs.close();
			ps.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return role;
	}

	public Role findById(long id) {
		Role role = null;
		ResultSet rs = null;
		try {
			String sql = "Select * from user_role where roleid=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				role = getRoleFromResultset(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return role;
	}

	public ArrayList<Role> findRolesByGroupName(String groupname) {
		ArrayList<Role> roles = new ArrayList<Role>();
		if (groupname == null) {
			return null;
		}
		ResultSet rs = null;
		try {
			String sql = "Select a.roleid, a.name, a.description "
					+ "FROM user_role a , user_role_members b, user_group c "
					+ "WHERE a.roleid = b.roleid AND b.groupid = c.groupid "
					+ "AND c.name=?";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, groupname);
			rs = ps.executeQuery();
			while (rs.next()) {
				roles.add(getRoleFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Wrap into DaoException()
			e.printStackTrace();
		}
		return roles;
	}

	public ArrayList<Role> findRolesByUserEmail(String email) {
		ArrayList<Role> roles = new ArrayList<Role>();
		if (email == null) {
			return null;
		}
		ResultSet rs = null;
		try {
			String sql = "Select a.roleid, a.name, a.description "
					+ "FROM user_role a where a.roleid in "
					+ " (select b.roleid from user_role_members b "
					+ "  where b.groupid in (select groupid from user_group_members c "
					+ "  where c.userid in (select d.userid from user d where d.email=?)))";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			while (rs.next()) {
				roles.add(getRoleFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Wrap into DaoException()
			e.printStackTrace();
		}
		return roles;
	}
	

	public ArrayList<Role> findRolesByUserId(long userid) {
		ArrayList<Role> roles = new ArrayList<Role>();

		ResultSet rs = null;
		try {
			String sql = "Select a.roleid, a.name, a.description "
					+ "FROM user_role a where a.roleid in "
					+ " (select b.roleid from user_role_members b "
					+ "  where b.groupid in (select groupid from user_group_members c "
					+ "  where c.userid in (select d.userid from user d where d.userid=?)))";
			PreparedStatement ps = this.connection.prepareStatement(sql);
			ps.setLong(1, userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				roles.add(getRoleFromResultset(rs));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Wrap into DaoException()
			e.printStackTrace();
		}
		return roles;
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
					.executeQuery("SELECT count(roleid) AS totalcount FROM user_role");
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

	private Role getRoleFromResultset(ResultSet rs) throws SQLException {
		Role role = new Role();
		role.setRoleid(rs.getLong("roleid"));
		role.setName(rs.getString("name"));
		role.setDescription(rs.getString("description"));
		return role;
	}

	public long insert(Role role) {
		long result = 0;
		try {
			String sql = "INSERT INTO user_role " + "(name,created) "
					+ "VALUES (?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, role.getName());
			ps.setTimestamp(2, this.getCurrentTimeStamp());
			ps.executeUpdate();
			ps.close();
			result = this.getLastUpdateId();
			role.setRoleid(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Role> listAll() {
		ArrayList<Role> roleList = new ArrayList<Role>();
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM user_role ";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				roleList.add(this.getRoleFromResultset(rs));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roleList;
	}

	public void update(Role role) {
		try {
			String sql = "UPDATE user_role " + "SET name=? " + "WHERE roleid=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, role.getName());
			ps.setLong(2, role.getRoleid());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
