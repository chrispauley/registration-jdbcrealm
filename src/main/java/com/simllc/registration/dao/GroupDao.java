package com.simllc.registration.dao;

import java.util.ArrayList;


import com.simllc.registration.model.Group;
import com.simllc.registration.model.Role;

public interface GroupDao {
	
	/**
	 * Create a new group in the group table.
	 * @param group
	 * @return groupid for the new group.
	 */
	public long insert(Group group);
	
	/**
	 * Delete a group from the able by groupid
	 * @param groupid
	 */
	public void delete(long groupid);
	
	/**
	 * findById
	 * @param groupid from group table
	 * @return Group
	 */
	public Group findById(long groupid);
	
	/**
	 * findByName
	 * @param address from group table
	 * @return Group
	 */
	public Group findByName(String groupname);
	
	/**
	 * Used to manage user ACL.
	 * @param email
	 * @return ArrayList<Group>
	 */
	public ArrayList<Group> findGroupsByEmail(String email);
	
	/**
	 * Total count in the group table.
	 * @return long
	 */
	public long getTotalCount();
	
	/**
	 * Fetch one long ArrayList of Group.
	 * @return
	 */
	public ArrayList<Group> listAll();
	
	/**
	 * Updates a group object in the database.
	 * @param group
	 */
	public void update(Group group);
}
