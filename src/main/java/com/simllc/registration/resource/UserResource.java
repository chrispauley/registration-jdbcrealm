package com.simllc.registration.resource;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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


@Path("/user")
public class UserResource extends BaseResource {
	private UserDao dao;
	
	public UserResource(){
		super();
		if(dao==null){
			try {
				dao = new UserDaoImpl(this.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * @param uriInfo
	 *            @Context UriInfo
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
		url += "/apidocs.html#user";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}
	
	@Path("{userid}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User getById(@PathParam("userid") long userid) {
		return dao.findById(userid);
	}


	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User getByEmail(@QueryParam("email") String email) {
		return dao.findByEmail(email);
	}
	
	@DELETE
	@Path("{userid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void remove(@PathParam("userid") Long userid) {
		dao.delete(userid);
		return;
	}

}
