package com.simllc.catalina.valves;


import java.io.IOException;
import java.security.Principal;
import java.util.StringTokenizer;

import javax.management.JMException;
import javax.management.ObjectName;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.catalina.Realm;
//import org.apache.catalina.Session;
//import org.apache.catalina.authenticator.Constants;
//import org.apache.catalina.connector.Request;
//import org.apache.catalina.connector.Response;
//import org.apache.catalina.deploy.LoginConfig;

public class CookieAuthenticatorValve {

	
	public CookieAuthenticatorValve() {
		super();
	}
	
	

	protected String getCookieValue(Cookie[] cookies, int numCookies,
			String token) {
		for (int i = 0; i < numCookies; i++) {
			Cookie cookie = cookies[i];
			System.out.println("Matching cookieToken:" + token + " with cookie name="
					+ cookie.getName());
			if (token.equals(cookie.getName())) {
//				if (trace)
//					log.trace("Cookie-" + token + " value=" + cookie.getValue());
				return cookie.getValue();
			}
		}
		return null;
	}
}
