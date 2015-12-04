package com.simllc.registration.resource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.simllc.registration.dao.UserDao;
import com.simllc.registration.dao.mysql.UserDaoImpl;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.User;

@Path("/users")
public class UsersResource extends BaseResource {
	private UserDao dao;
	@Context
	HttpServletRequest servletRequest;

	public UsersResource() {
		super();
		Connection con = null;
		if (dao == null) {
			try {
				con = this.getConnection();
				dao = new UserDaoImpl(con);
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
	@Produces({ MediaType.TEXT_PLAIN })
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
		url += "/apidocs.html#users";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ArrayList<User> listAllPaged(
			@QueryParam("start") @DefaultValue("1") int start,
			@QueryParam("limit") @DefaultValue("10") int limit,
			@QueryParam("orderby") @DefaultValue("id") String orderby) {
		if (orderby != null) {
			// TODO switch this for valid orderby clauses
		}
		String auth = servletRequest.getHeader("Authorization");
		if (auth != null) {
			User user = loginAccount(auth);
		}
		if(auth==null){
			// handle gracefully
		}

		return dao.listAllPaged(start, limit, orderby);
	}

	private User loginAccount(String auth) {

		return null;
	}

}
