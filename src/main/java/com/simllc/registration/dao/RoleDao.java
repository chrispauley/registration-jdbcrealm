package com.simllc.registration.dao;

import java.util.ArrayList;


import com.simllc.registration.model.*;

public interface RoleDao {
	
	/**
	 * Create a new role in the role table.
	 * @param role
	 * @return roleid for the new role.
	 */
	public long insert(Role role);
	
	/**
	 * Used for dbrealm authentication.
	 * @param email
	 * @param rolename
	 * @return boolean
	 */
	public boolean isUserInRole(String rolename, String email);
	
	/**
	 * Delete a role from the able by roleid
	 * @param roleid
	 */
	public void delete(long roleid);
	
	/**
	 * findById
	 * @param roleid from role table
	 * @return Role
	 */
	public Role findById(long roleid);
	
	/**
	 * findByName
	 * @param address from role table
	 * @return Role
	 */
	public Role findByName(String rolename);
	
	/**
	 * Fetch an ArrayList of Role for a given Group.
	 * @param groupname 
	 * @return ArrayList<Role>
	 */
    public ArrayList<Role> findRolesByGroupName(String groupname);
    
    /**
     * Fetch all the roles for a user.
     * @param email
     * @return
     */
    public ArrayList<Role> findRolesByUserEmail(String email);
    
    /**
     * Fetch all the roles for a user.
     * @param userid long
     * @return ArrayList<Role>
     */
    public ArrayList<Role> findRolesByUserId(long userid);
	
	/**
	 * Total count in the role table.
	 * @return long
	 */
	public long getTotalCount();
	
	/**
	 * Fetch one long ArrayList of Role.
	 * @return
	 */
	public ArrayList<Role> listAll();
	
	/**
	 * Updates a role object in the database.
	 * @param role
	 */
	public void update(Role role);
}
