package com.simllc.registration.resource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.simllc.registration.resource.BCrypt;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.User;
import com.simllc.registration.dao.*;
import com.simllc.registration.dao.mysql.UserDaoImpl;
import com.simllc.registration.mail.Notifier;

@Path("/account")
public class RegistrationResource extends BaseResource {
	private static String SIGNUP_URI = "../../../signup.jsp";
	private static String LOGIN_URI = "../../../login.jsp";
	private static String SIGNUP_COMPLETE_URI = "../../../signup_requested.jsp";
	private static String ACTIVATE_URI = "../../../activate.jsp";
	UserDao _dao;

	@Context UriInfo uriInfo;
	@Context Request request;
	@Context HttpServletRequest servletRequest;

	public RegistrationResource() {
		super();
		Connection con = null;
		try {
			con = this.getDS().getConnection();
			_dao = new UserDaoImpl(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param uriInfo
	 * @Context UriInfo
	 * @param hsr
	 *            HttpServletRequest
	 * @return Response
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response sayPlainText(@Context HttpServletRequest hsr) {
		UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
		Response.ResponseBuilder builder = Response
				.ok("This is the GET text/plain response for \n"
						+ absolute.clone().path("").build().toString()
						+ "\nPlease try OPTIONS for wadl of this resource.");
		String scheme = hsr.getScheme();
		String serverName = hsr.getServerName();
		int serverPort = hsr.getServerPort();
		String contextPath = hsr.getContextPath();

		String url = scheme + "://" + serverName + ":" + serverPort
				+ contextPath;
		url += "/apidocs.html#auth";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}

	/**
	 * post /registration/auth/account/forgot
	 * @param email
	 * @param username
	 * @param hsr
	 * @return
	 */
	@Path("/forgot")
	@POST
	public Response forgot(@FormParam("email") String email,
			@FormParam("username") String username,
			@Context HttpServletRequest hsr) {
		ResponseBuilder builder = null;

		User user = _dao.findByEmail(email);
		if (user == null) {
			throw new WebApplicationException(new Throwable("User for " + email
					+ " not found."), 400);
		} else {
			
			// Found the user. Create a temporary password and change User account state.
			String hashed = BCrypt.hashpw(email + "drowssap", BCrypt.gensalt());
			UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
			String resetUrl = absolute.clone().path("").build().toString()
					+ "/reset?token=" + hashed;
			System.out.println("resetUrl: " + resetUrl);
			
			user.setEmail(email);
			user.setPassword("");
			user.setTempPass(hashed);
			user.setTempPassActive(1);
			user.setTempPass(hashed);
			_dao.updateTempPassword(user.getUserid(), hashed);

			Notifier notifier = new Notifier();
//			String result = notifier.sendResetMessage(user, resetUrl);
			String result = notifier.sendResetHTMLMessage(user, resetUrl);
			builder = Response.ok("Password reset request accepted. \nPlease check your Email: " + email
					+ "\n<a href='" + resetUrl + "'> Activate Link</a>");
		}
		return builder.build();
	}

	// TODO: How is POST /resetPassword different than POST /signupSetPassword?  
	// User account roles? 
	/**
	 * POST /registration/auth/account/reset
	 * @param email
	 * @param username
	 * @param password
	 * @param hsr
	 * @return
	 */
	@Path("/activate")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	public Response resetPassword(@FormParam("email") String email,
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("confirm_password") String confirm_password,
			@Context HttpServletResponse servletResponse) throws IOException {

		ArrayList<String> errorList = new ArrayList<String>();
		User user = _dao.findByEmail(email);
		
		if (user == null) {
			errorList.add("User not found by email");
			servletRequest.setAttribute("errorList", errorList);
			servletResponse.sendRedirect("../../../activate.jsp");
		} else {
			if (!password.equals(confirm_password)) {
				errorList.add("Password and confirmed password do not match.");
			} else {
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
				_dao.updatePassword(user.getUserid(), hashedPassword);
			}
		}
		return Response.ok("updated user password"
				+ "\n<a href='/registration/login.jsp?email="+ email + "'>Login</a>").build();
	}

	/**
	 * GET /registration/auth/account/reset?token=XYZ
	 * Success==> reset_password.jsp
	 * which posts to /registration/auth/account/reset
	 * @param token
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@Path("/forgot/reset")
	@Produces({ MediaType.TEXT_HTML })
	@GET
	public Response resetWithToken(@QueryParam("token") String token,
			@Context HttpServletResponse servletResponse) throws IOException {
		System.out.println("activateUser: " + token);
		User user = _dao.findByToken(token);
		if (user != null) {
			System.out.println("Found user for: " + token);
			System.out.println("Hello again user: " + user.getEmail());
			_dao.activateUser(user.getUserid());
			this.servletRequest.setAttribute("message", "Hello again user: " + user.getEmail());
			servletResponse.sendRedirect("/registration/activate.jsp");
		} else {
			System.out.println("User not found or invalid token for: " + token);
			this.servletRequest.setAttribute("errorList", "User not found or invalid token for: " + token);
			servletResponse.sendRedirect("login.jsp");
		}
		return Response.ok().build();
	}	
	
	/**
	 * First step in the registration process. If successful, the user receives an Email with an 
	 * activationUrl. Next: activateUser()
	 * 
	 * @param email
	 * @param username
	 * @param password
	 * @param uriInfo
	 * @param hsr
	 * @return 1. Validate input 1.1 If email not found send them to the
	 *         registration page 1.2 If password is invalid send error response.
	 *         2 Update user record, assign to a security group.
	 * @throws IOException
	 */

	@Path("/signup")
	@POST
	@Produces({ MediaType.TEXT_HTML })
	public Response signup(@FormParam("email") String email,
			@FormParam("username") String username,
			@Context HttpServletRequest hsr,
			@Context HttpServletResponse servletResponse) throws IOException {
		ResponseBuilder builder = null;

		ArrayList<String> errorList = new ArrayList<String>();
		if(email==null || !validEmail(email)){
			errorList.add("Invalid or missing email.");
			hsr.setAttribute("errorList", errorList);
			servletResponse.sendRedirect(RegistrationResource.SIGNUP_URI);
			builder = Response.ok("Try again...");
		}

		User user = _dao.findByEmail(email);
		if (user != null) {
			errorList.add("Email account exists");
			hsr.setAttribute("errorList", errorList);
			servletResponse.sendRedirect(RegistrationResource.SIGNUP_URI);
			builder = Response.status(Response.Status.PRECONDITION_FAILED);
		} else {
			String hashed = BCrypt.hashpw(email + "drowssap", BCrypt.gensalt());
			UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
			String activationUrl = absolute.clone().path("").build().toString()
					+ "/activate?token=" + hashed;
			user = new User();
			user.setEmail(email);
			user.setPassword("");
			user.setTempPass(hashed);
			user.setTempPassActive(1);
			user.setLevelAccess("0");
			user.setTempPass(hashed);
			long userid = this._dao.insert(user);
			user.setUserid(userid);

			Notifier notifier = new Notifier();
			String result = notifier.sendActivationMessage(user, activationUrl);
			if(!result.contains(result)){
				errorList.add("Problem occurred sending an activation url to " + email);
				hsr.setAttribute("errorList", errorList);
				servletResponse.sendRedirect(RegistrationResource.SIGNUP_URI);
				// TODO Email exists error.
				
			} else {
				hsr.setAttribute("errorList", null);
				hsr.setAttribute("message", "Activation email sent to " + email);
				servletResponse.sendRedirect(RegistrationResource.SIGNUP_COMPLETE_URI);
				builder = Response.ok("activation email sent");
			}
		}
		return builder.build();
	}


	/**
	 * Second step in the registration process. User clicked on an activationUrl. The GET will
	 * activate the user account and prompt the User to define a password.
	 * Next: POST /registration/auth/account/activate 
	 * 
	 * @Path("/signup/activate")
	 * @param token
	 * @param hsr
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@Produces({ MediaType.TEXT_HTML })
	@GET
	public Response activateUser(@QueryParam("token") String token,
			@Context HttpServletResponse servletResponse) throws IOException {

		System.out.println("activateUser: " + token);
		User user = _dao.findByToken(token);
		if (user != null) {
			System.out.println("found user for: " + token);
			System.out.println("Welcome new user: " + user.getEmail());
			_dao.activateUser(user.getUserid());
			this.servletRequest.setAttribute("message", "Welcome new user: " + user.getEmail());
			servletResponse.sendRedirect(RegistrationResource.ACTIVATE_URI);
		} else {
			System.out.println("User not found or invalid token for: " + token);
			this.servletRequest.setAttribute("errorList", "User not found or invalid token for: " + token);
			servletResponse.sendRedirect(RegistrationResource.SIGNUP_URI);
		}
		return null;
	}

	 /* Third step in the registration process. User POST'd /registration/auth/account/activate 
	  * to send a new password. 
	  * Next--> Login.
	  * 
	 * @param email
	 * @param username
	 * @param password
	 * @param confirm_password
	 * @param servletResponse
	 * @return
	 * @throws IOException
	 */
	@Path("/signup/activate")
	@Produces({ MediaType.TEXT_HTML })
	@POST
	public Response signupSetPassword(@FormParam("email") String email,
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("confirm_password") String confirm_password,
			@Context HttpServletResponse servletResponse) throws IOException {

		ArrayList<String> errorList = new ArrayList<String>();
		User user = _dao.findByEmail(email);
		
		if (user == null) {
			errorList.add("User not found by email");
			servletRequest.setAttribute("errorList", errorList);
			servletResponse.sendRedirect("../../../activate.jsp");
		} else {
			if (password != confirm_password) {
				errorList.add("Password and confirmed password do not match.");
			} else {
				String hashedPassword = BCrypt.hashpw(password + "drowssap", BCrypt.gensalt());
				_dao.updatePassword(user.getUserid(), hashedPassword);
				servletRequest.setAttribute("message", "success");
				servletResponse.sendRedirect("../../../activated.jsp");
			}
		}
		return Response.ok("updated user password").build();
	}

	private boolean validUser(User user) {
		return true;
	}

	private boolean validEmail(String email) {
		return true;
	}

}
