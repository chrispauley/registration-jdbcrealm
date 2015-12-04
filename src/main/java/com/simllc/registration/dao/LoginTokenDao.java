package com.simllc.registration.dao;

import java.util.ArrayList;


import com.simllc.registration.model.*;

public interface LoginTokenDao {
	
	/**
	 * Create a new LoginToken in the user_login_token table.
	 * @param long userid
	 * @return new LoginToken.
	 */
	public LoginToken create(long userid);
	
	/**
	 * Create a new LoginToken in the user_login_token table.
	 * @param LoginToken
	 * @return id for the new LoginToken.
	 */
	public long insert(LoginToken token);

	public boolean isValidToken(LoginToken loginToken);
	
	public void delete(long id);
	
	public LoginToken findById(long id);
	
	public LoginToken findByToken(String token);
	
	/**
	 * Fetch an ArrayList of LoginToken for a given user.
	 * @param userid for the tokens 
	 * @return ArrayList<LoginToken>
	 */
    public ArrayList<LoginToken> findTokensByUserid(Long userid);
    
    /**
     * Fetch all the roles for a user.
     * @param email
     * @return
     */
    public ArrayList<LoginToken> findTokensByUserEmail(String email);
	
	/**
	 * Total count in the user_login_token table.
	 * @return long
	 */
	public long getTotalCount();
	
	/**
	 * Fetch one long ArrayList of LoginToken.
	 * @return
	 */
	public ArrayList<LoginToken> listAll();
	
}
