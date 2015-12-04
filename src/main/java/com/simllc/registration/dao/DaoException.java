package com.simllc.registration.dao;

public class DaoException extends Exception {
	private String msg = null;
	private Exception e = null;
	
	public DaoException(String msg, Exception e){
		super();
	}

}
