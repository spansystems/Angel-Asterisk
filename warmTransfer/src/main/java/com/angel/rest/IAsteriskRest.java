package com.angel.rest;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("/accounts/{account-id}/pageID/{page-id}/agentID/{agent-id}")
public interface IAsteriskRest
{

	/**
	 * 
	 * @return JSON as String containing list of active calls.
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	Response processRequest(@PathParam("account-id") String accountId, @PathParam("page-id") String pageId,
			@PathParam("agent-id") String agentId, InputStream data);
}
