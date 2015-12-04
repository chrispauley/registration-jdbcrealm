package com.simllc.registration.dao;

import java.util.ArrayList;


import com.simllc.registration.model.GroupMember;
import com.simllc.registration.model.User;

public interface GroupMembersDao {
	
	/**
	 * Add a user to a group.
	 * @param groupid
	 * @param userid
	 * @return
	 */
	public long insert(long groupid, long userid);
	
	/**
	 * Remove a user from a group.
	 * @param groupid
	 * @param userid
	 */
	public void delete(long groupid, long userid);
		
	
	/**
	 * Fetch a list of groups for a given userid.
	 * @param userid
	 * @return ArrayList<Group>
	 */
	public ArrayList<GroupMember> listGroupMembersByUserId(long userid);
	
	/**
	 * Fetch a list of users for a given group.
	 * @param groupid
	 * @return ArrayList<User>
	 */
	public ArrayList<User> listUsersByGroupId(long groupid);

}
