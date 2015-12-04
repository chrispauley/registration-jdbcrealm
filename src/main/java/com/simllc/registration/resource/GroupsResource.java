package com.simllc.registration.resource;

import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.security.RolesAllowed;

import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import com.simllc.registration.dao.GroupDao;
import com.simllc.registration.dao.mysql.GroupDaoImpl;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.Group;

@Path("/groups")
public class GroupsResource extends BaseResource {
	private GroupDao dao;
	@Context HttpServletRequest servletRequest;
	@Context SecurityContext security;

	public GroupsResource() {
		super();
		Connection con = null;
		if (dao == null) {
			try {
				con = this.getConnection();
				dao = new GroupDaoImpl(con);
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
		url += "/apidocs.html#roles";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}

	@GET
	@RolesAllowed({"admin","registered_users", "administrator"})
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ArrayList<Group> listAll() {
		System.out.println("security: " + security.isSecure());
		System.out.println("AuthenticationScheme: " + security.getAuthenticationScheme());
		Principal principal = security.getUserPrincipal();
		if (principal != null) {
			System.out.println("principal: " + principal.getName());
		}
		return dao.listAll();
	}
	

	// groups/user?email=user@domain.com
	@Path("/user")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Group> findGroupsByUserEmail1(@QueryParam("email") String email) {
		return dao.findGroupsByEmail(email);
	}
	
	// groups/user/user@domain.com
	@Path("/user/{email}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Group> findGroupsByUserEmail2(@PathParam("email") String email) {
		return dao.findGroupsByEmail(email);
	}

}
