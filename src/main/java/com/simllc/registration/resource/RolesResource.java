package com.simllc.registration.resource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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

import com.simllc.registration.dao.RoleDao;
import com.simllc.registration.dao.mysql.RoleDaoImpl;
import com.simllc.registration.model.Group;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.Role;

@Path("/roles")
public class RolesResource extends BaseResource {
	private RoleDao dao;
	@Context
	HttpServletRequest servletRequest;

	public RolesResource() {
		super();
		Connection con = null;
		if (dao == null) {
			try {
				con = this.getConnection();
				dao = new RoleDaoImpl(con);
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
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ArrayList<Role> listAll() {
		return dao.listAll();
	}
	
	// roles/user?email=user@domain.com
	// /registration/auth/roles/user?email=user2@gpxdb.com
	@Path("/user")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Role> findGroupsByUserEmail1(@QueryParam("email") String email) {
		return dao.findRolesByUserEmail(email);
	}

	// roles/user/user@domain.com
	@Path("/user/{email}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Role> findGroupsByUserEmail2(@PathParam("email") String email) {
		return dao.findRolesByUserEmail(email);
	}
}
