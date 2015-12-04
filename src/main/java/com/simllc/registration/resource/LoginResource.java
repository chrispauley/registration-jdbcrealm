package com.simllc.registration.resource;

import java.sql.SQLException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.simllc.registration.resource.BCrypt;
import com.simllc.registration.dao.UserDao;
import com.simllc.registration.dao.mysql.UserDaoImpl;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.User;

@Path("/login")
public class LoginResource extends BaseResource {

	private UserDao dao;

	public LoginResource() {
		super();
		if (dao == null) {
			try {
				dao = new UserDaoImpl(this.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
	public Response sayPlainText(@Context UriInfo uriInfo,
			@Context HttpServletRequest hsr) {
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
		url += "/apidocs.html#login";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response loginUser(@FormParam("email") String email,
			@FormParam("password") String password) {
		ResponseBuilder builder = null;
		System.out.println("loginUser:  email=" + email + " password=" + password);
		if(!validEmail(email)){
			builder = Response.status(Response.Status.BAD_REQUEST);
			return builder.build();
		}
		if(!validPassword(password)){
			builder = Response.status(Response.Status.BAD_REQUEST);
			return builder.build();
		}
		
		User user = dao.findByEmail(email);
		if (user == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			return builder.build();
		} else {
			if(!validPassword(user.getPassword())){
				builder = Response.status(Response.Status.UNAUTHORIZED);
				return builder.build();
			}
			
			String candidate = password;
			if (BCrypt.checkpw(candidate, user.getPassword())) {
				return Response.ok("ok")
	               .cookie(new NewCookie("name", "Hello, world!"))
	               .build();
			} else {
				builder = Response.status(Response.Status.UNAUTHORIZED);
			}
		}
		return builder.build();
	}

	private boolean validPassword(String password) {
		boolean result = true;
		if(password==null){
			result = false;
		}
		if(password.trim().length()<2){
			result = false;
		}
		return result;
	}

	private boolean validEmail(String email) {
		boolean result = false;
		try {
		    new InternetAddress(email).getAddress();
		    result = true;
		} catch (AddressException e) {
		    result = false;
		}
		return result;
	}

}
