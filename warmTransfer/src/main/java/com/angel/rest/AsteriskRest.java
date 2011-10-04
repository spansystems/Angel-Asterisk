/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angel.agent.Agent;
import com.angel.events.BridgeEvent;
import com.angel.events.HandOverCallEvent;
import com.angel.events.HangUpEvent;
import com.angel.events.IEvents;
import com.angel.events.JoinCallEvent;
import com.angel.events.ParkEvent;
import com.angel.events.UnParkEvent;
import com.angel.manager.AgentMap;

/**
 * Asterisk restlet.
 * 
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class AsteriskRest implements IAsteriskRest
{
	private static final Logger LOG = LoggerFactory.getLogger(AsteriskRest.class);
	private ObjectMapper mapper = new ObjectMapper();
	private static AsteriskEventRouter router = AsteriskEventRouter.getInstance();
	private String responseString = null;
	private Response response;
	InputJson json;

	@SuppressWarnings({ "finally" })
	public Response processRequest(@PathParam("account-id") String accountId, @PathParam("page-id") String pageId,
			@PathParam("agent-id") String agentId, InputStream data)
	{

		LOG.info("Rest request received in processRequest method");

		try
		{
			json = mapper.readValue(data, InputJson.class);
			processRequest(json);
		}
		catch (IOException ex)
		{
			LOG.warn("Json validation failed");
			responseString = "Not able to read Json format";
			sendJsonResponse(responseString, Status.NOT_ACCEPTABLE);
		}
		finally
		{
			return response;
		}
	}

	private void processRequest(InputJson json)
	{
		try
		{
			final String requestEvent = json.getEvent();
			final Events event = Events.valueOf(requestEvent);
			final Agent agent = AgentMap.getAgentMap().getAgent(json.getData().getAgent());
			IEvents request;

			if (null != agent)
			{
				response = Response.ok().build();
				switch (event)
				{
					case HANG_UP_CALL:
						if (null != json.getData().getDestinationAgent())
						{
							request = new HangUpEvent(json, agent);
							router.route(request);
						}
						else
						{
							LOG.warn("Destination agent id not found(Hang up call");
							responseString = "Destination agent id notfound(Hangup call)";
							sendJsonResponse(responseString, Status.NOT_ACCEPTABLE);
						}
						break;
					case PARK_CALL:
						request = new ParkEvent(json, agent);
						router.route(request);
						break;
					case BRIDGE_CALL:
						if (null != json.getData().getDestinationAgent())
						{
							request = new BridgeEvent(json, agent);
							router.route(request);
						}
						else
						{
							LOG.warn("Destination agent id notfound(Bridge call)");
							responseString = "Destination agent id notfound(Bridge call)";
							sendJsonResponse(responseString, Status.NOT_ACCEPTABLE);
						}
						break;
					case JOIN_CALL:
						request = new JoinCallEvent(json, agent);
						router.route(request);
						break;
					case RESUME_CALL:
						request = new UnParkEvent(json, agent);
						router.route(request);
						break;
					case HAND_OVER_CALL:
						if (null != json.getData().getDestinationAgent())
						{
							request = new HandOverCallEvent(json, agent);
							router.route(request);
						}
						else
						{
							LOG.warn("Destination agent id notfound(Hand Over call)");
							responseString = "Destination agent id notfound(Hand Over call)";
							sendJsonResponse(responseString, Status.NOT_ACCEPTABLE);
						}
						break;
					default:
						responseString = "action unidentified";
						sendJsonResponse(responseString, Status.NOT_ACCEPTABLE);

				}
			}
			else
			{
				LOG.warn("Destination agent id not found");
				responseString = "Agent not found";
				sendJsonResponse(responseString, Status.FORBIDDEN);
			}
		}
		catch (Exception e)
		{
			LOG.warn("Exception while processing event", e);
			responseString = "Internal server error";
			sendJsonResponse(responseString, Status.INTERNAL_SERVER_ERROR);
		}

	}

	private void sendJsonResponse(final String responseString, Status status)
	{

		final OutputJson jsonOut = new OutputJson();
		jsonOut.setResponse(responseString);
		try
		{
			final String outStr = mapper.writeValueAsString(jsonOut);
			response = Response.status(Status.FORBIDDEN).entity(outStr).build();
		}
		catch (Exception ex)
		{
			LOG.warn("Not able to write in output json class ", ex);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@SuppressWarnings("finally")
	@Override
	public Response processRequest(@PathParam("account-id") String accountId, @PathParam("page-id") String pageId,
			@PathParam("agent-id") String agentId, @FormParam("eventData") String postData)
	{
		try
		{
			json = mapper.readValue(postData, InputJson.class);
			processRequest(json);
		}
		catch (IOException e)
		{
			LOG.warn("Json validation failed");
			e.printStackTrace();
			responseString = "Not able to read Json format";
			sendJsonResponse(responseString, Status.NOT_ACCEPTABLE);
		}
		finally
		{
			return response;
		}
	}
}
