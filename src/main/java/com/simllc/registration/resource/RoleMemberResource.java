package com.simllc.registration.resource;

import java.sql.SQLException;
import java.util.ArrayList;

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

import com.simllc.registration.dao.RoleMembersDao;
import com.simllc.registration.dao.mysql.RoleMembersDaoImpl;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.Role;
import com.simllc.registration.model.Group;
import com.simllc.registration.model.GroupMember;


@Path("/rolemember")
public class RoleMemberResource extends BaseResource {
	private RoleMembersDao dao;
	
	public RoleMemberResource(){
		super();
		if(dao==null){
			try {
				dao = new RoleMembersDaoImpl(this.getConnection());
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
		url += "/apidocs.html#rolemember";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}

	@Path("{roleid}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<GroupMember> findByName(@PathParam("roleid") Long roleid) {
		return dao.listGroupMembersByRole(roleid);
	}
	
//	@DELETE
//	@Path("{roleid}")
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public void remove(@PathParam("roleid") Long roleid) {
//		dao.delete(roleid, groupid)
//		return;
//	}

}
