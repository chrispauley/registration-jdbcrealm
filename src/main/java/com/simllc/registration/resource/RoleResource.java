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

import com.simllc.registration.dao.RoleDao;
import com.simllc.registration.dao.mysql.RoleDaoImpl;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.Role;

@Path("/role")
public class RoleResource extends BaseResource {
	private RoleDao dao;

	public RoleResource() {
		super();
		if (dao == null) {
			try {
				dao = new RoleDaoImpl(this.getConnection());
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
		url += "/apidocs.html#role";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}

	@Path("{rolename}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Role findByName(@PathParam("rolename") String rolename) {
		return dao.findByName(rolename);
	}

	//   /role/administrator/user/chris@gpxdb.com
	@Path("/{rolename}/user/{email}")
	@GET
	public Response isUserInRole(@PathParam("rolename") String rolename,
			@PathParam("email") String email) {
		Response.ResponseBuilder builder;
		if (dao.isUserInRole(rolename, email)) {
			builder = Response.ok("User is in role.");
		} else {
//			builder = Response.status(Response.Status.NO_CONTENT);
			builder = Response.status(204);
		}
		return builder.build();
	}

	@DELETE
	@Path("{roleid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void remove(@PathParam("roleid") Long roleid) {
		dao.delete(roleid);
		return;
	}

}
