package com.angel.rest;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path(IAsteriskRest.basePath)
public interface IAsteriskRest {

	String basePath = "/accounts/{account-id}/pageID/{page-id}";
	String agentPath = "/agentID/{agent-id}";

	/**
	 * 
	 * @return JSON as String containing list of active calls.
	 */
	@Path(agentPath)
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response processRequest(@PathParam("account-id") String accountId,
			@PathParam("page-id") String pageId,
			@PathParam("agent-id") String agentId, InputStream data);

	@Path(agentPath)
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.WILDCARD)
	Response processRequest(@PathParam("account-id") String accountId,
			@PathParam("page-id") String pageId,
			@PathParam("agent-id") String agentId,
			@FormParam("eventData") String postData);
}
