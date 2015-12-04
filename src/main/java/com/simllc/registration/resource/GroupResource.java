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

import com.simllc.registration.dao.GroupDao;
import com.simllc.registration.dao.RoleDao;
import com.simllc.registration.dao.mysql.GroupDaoImpl;
import com.simllc.registration.dao.mysql.RoleDaoImpl;
import com.simllc.registration.model.RelLink;
import com.simllc.registration.model.Group;
import com.simllc.registration.model.Role;


@Path("/group")
public class GroupResource extends BaseResource {
	private GroupDao groupDao;
	private RoleDao roleDao;
	
	public GroupResource(){
		super();
		if(groupDao==null){
			try {
				groupDao = new GroupDaoImpl(this.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(roleDao==null){
			try {
				roleDao = new RoleDaoImpl(this.getConnection());
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
		url += "/apidocs.html#role";
		builder.header("Link", new RelLink("help", url, "text/html"));
		return builder.build();
	}

	@Path("{groupname}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Group findByName(@PathParam("groupname") String groupname) {
		return groupDao.findByName(groupname);
	}
	
	@Path("{groupname}/roles")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ArrayList<Role> findRolesByGroupName(@PathParam("groupname") String groupname) {
		return roleDao.findRolesByGroupName(groupname);
	}
	
	@DELETE
	@Path("{groupid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void remove(@PathParam("groupid") Long groupid) {
		groupDao.delete(groupid);
		return;
	}

}
