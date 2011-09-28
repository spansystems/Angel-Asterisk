/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.rest;

import com.angel.agent.Agent;
import com.angel.events.BridgeEvent;
import com.angel.events.HandOverCallEvent;
import com.angel.events.HangUpEvent;
import com.angel.events.IAsteriskEvent;
import com.angel.events.JoinCallEvent;
import com.angel.events.ParkEvent;
import com.angel.events.UnParkEvent;
import com.angel.manager.AgentMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.codehaus.jackson.map.ObjectMapper;


@Path("/accounts/{account-id}/pageID/{page-id}/agentID/{agent-id}")
public class AsteriskRest {
//    static final Logger logger = Logger.getLogger(AsteriskRest.class);
    private ObjectMapper mapper = new ObjectMapper();
    static AsteriskEventRouter router = AsteriskEventRouter.getInstance();
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processRequest(@PathParam("account-id") String accountId, @PathParam("page-id") String pageId,
            @PathParam("agent-id") String agentId, InputStream data) {
        Response response = null;
        boolean check = true;
        String responseString = null;
        InputJson json = null;
        try {
            json = mapper.readValue(data, InputJson.class);
            mapper.writeValueAsString(json);
        } catch (IOException ex) {
            check = false;
            Logger.getLogger(AsteriskRest.class.getName()).log(Level.SEVERE, "Json validation failed", ex);
            responseString = "Not able to read Json format";
            sendJsonResponse(response, responseString);
        }
        try {
            String requestEvent = json.getEvent();
            Events event = Events.valueOf(requestEvent);
            //String agentId = json.getData().getAgent();
            Agent agent = AgentMap.getAgent(agentId);
            IAsteriskEvent request;
            /*
             * Validating JSON format.
             * Creating new thread for each request.
             * Returning the corresponding response.
             */

            if (null != agent && check == true) {
                response = Response.ok().build();
                switch (event) {
                    case HANG_UP_CALL:
                        if (null != json.getData().getDestinationAgent()) {
                            request = new HangUpEvent(json, agent);
                            router.route(request);
                        } else {
                            Logger.getLogger(AsteriskRest.class.getName()).log(Level.SEVERE, "Destination agent id not "
                                    + "found(Hang up call)");
                            response = Response.status(Status.NO_CONTENT).build();
                        }
                        break;
                    case PARK_CALL:
                        request = new ParkEvent(json, agent);
                        router.route(request);
                        break;
                    case BRIDGE_CALL:
                        if (null != json.getData().getDestinationAgent()) {
                            request = new BridgeEvent(json, agent);
                            router.route(request);
                        } else {
                            Logger.getLogger(AsteriskRest.class.getName()).log(Level.SEVERE, "Destination agent id not "
                                    + "found(Bridge call)");
                            response = Response.status(Status.NO_CONTENT).build();
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
                        if (null != json.getData().getDestinationAgent()) {
                            request = new HandOverCallEvent(json, agent);
                            router.route(request);
                        } else {
                            Logger.getLogger(AsteriskRest.class.getName()).log(Level.SEVERE, "Destination agent id not "
                                    + "found(Hand over call)");
                            response = Response.status(Status.NO_CONTENT).build();
                        }
                        break;
                    default:
                        responseString = "action unidentified";
                        sendJsonResponse(response, responseString);

                }
            } else {
                response = Response.status(Status.BAD_REQUEST).build();
                Logger.getLogger(AsteriskRest.class.getName()).log(Level.SEVERE, "Destination agent id given in URL "
                        + "not found");
                responseString = "Agent not found";
                sendJsonResponse(response, responseString);
            }
        } catch (Exception e) {
            Logger.getLogger(AsteriskRest.class.getName()).log(Level.SEVERE, "exception while processing event", e);            
        } finally {
            return response;
        }
    }

    private void sendJsonResponse(Response response, String responseString) {
        OutputJson jsonOut = new OutputJson();
        jsonOut.setResponse(responseString);
        try {
            String outStr = mapper.writeValueAsString(jsonOut);
            response = Response.status(Status.BAD_REQUEST).entity(outStr).build();
        } catch (Exception ex) {
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
            Logger.getLogger(AsteriskRest.class.getName()).log(Level.SEVERE, "Not able to write in output "
                    + "json class", ex);
        }
    }
}
