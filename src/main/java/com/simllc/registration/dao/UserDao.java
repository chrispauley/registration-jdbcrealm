package com.simllc.registration.dao;

import java.util.ArrayList;


import com.simllc.registration.model.*;

public interface UserDao {
	
	/**
	 * Unlocks the user account. Changes to active status.
	 * @param userid
	 * @return
	 */
	public void activateUser(long userid);
	
	/**
	 * Locks the user account. Changes to inactive status.
	 * @param userid
	 */
	public void deactivateUser(long userid);
	
	/**
	 * Delete a user from the able by userid
	 * @param userid
	 */
	public void delete(long userid);
	
	/**
	 * findByEmail
	 * @param email Email address from user table
	 * @return User
	 */
	public User findByEmail(String email);
	
	/**
	 * findById
	 * @param userid from user table
	 * @return User
	 */
	public User findById(long userid);
	
	/**
	 * Locate a user by the activation token
	 * @param token
	 * @return
	 */
	public User findByToken(String temp_pass);
	
	/**
	 * Returns the salt value for a user's email
	 * @param email
	 * @return
	 */
	public String getSalt(String email);
	
	/**
	 * Total count in the user table.
	 * @return long
	 */
	public long getTotalCount();
	
	/**
	 * Create a new user in the user table.
	 * @param user
	 * @return userid for the new user.
	 */
	public long insert(User user);
	/**
	 * Fetch one long ArrayList of User.
	 * @return
	 */
	public ArrayList<User> listAll();

	/**
	 * Fetch ArrayList of User objects, paged.
	 * @param start First page.
	 * @param offset Number per page.
	 * @param orderby Sort order = [username | userid]
	 * @return
	 */
	public ArrayList<User> listAllPaged(long start, long offset, String orderby);
	
	/**
	 * Update user data in the database.
	 * Includes all values.
	 * @param user
	 */
	public void update(User user);
	
	/**
	 * Updates the last login date in the database
	 * @param long userid
	 */
	public void updateLastLoginDate(long userid);
	
	/**
	 * Updates the last login date in the database
	 * @param long userid
	 */
	public void logout(long userid);
	
	/**
	 * Updates the password in the database
	 * @param long userid
	 * @param String hashedPassword
	 */
	public void updatePassword(long userid, String hashedPassword);
	
	/**
	 * Updates the temporary password in the database
	 * @param long userid
	 * @param String hashedPassword
	 */
	public void updateTempPassword(long userid, String hashedPassword);
	
	
	public String updateSharedSecret(long userid);
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public boolean verifyUser(String email, String password);
}
