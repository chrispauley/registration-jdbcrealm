package com.simllc.registration.dao;

import java.util.ArrayList;


import com.simllc.registration.model.*;

public interface RoleMembersDao {
	
	/**
	 * Add a group to a role.
	 * @param roleid
	 * @param groupid
	 * @return
	 */
	public long insert(long roleid, long groupid);
	
	/**
	 * Remove a group from a role.
	 * @param roleid
	 * @param groupid
	 */
	public void delete(long roleid, long groupid);
		
	
	/**
	 * Fetch a list of groups for a given userid.
	 * @param userid
	 * @return ArrayList<Group>
	 */
	public ArrayList<GroupMember> listGroupMembersByRole(long roleid);
	
	/**
	 * Fetch a list of users for a given group.
	 * @param groupid
	 * @return ArrayList<Role>
	 */
	public ArrayList<Role> listRolesByGroup(long groupid);

}
