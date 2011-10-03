/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.rest;

import java.io.IOException;
import java.io.InputStream;

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
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class AsteriskRest implements IAsteriskRest {
    private static final Logger LOG = LoggerFactory.getLogger(AsteriskRest.class);
    private ObjectMapper mapper = new ObjectMapper();
    private static AsteriskEventRouter router = AsteriskEventRouter.getInstance();
    

    @SuppressWarnings({"finally"})
    public Response processRequest(String accountId, String pageId, String agentId, InputStream data) {
        LOG.debug("Rest request received in processRequest method");
        // return
        // "<html lang=\"en\"><body><h1>Asterisk Latest Implementation</body></h1></html>";
        Response response = null;
        boolean check = true;
        String responseString = null;
        InputJson json = null;
        try {
            json = mapper.readValue(data, InputJson.class);
            mapper.writeValueAsString(json);
        } catch (IOException ex) {
            check = false;
            LOG.warn("Json validation failed");
            responseString = "Not able to read Json format";
            sendJsonResponse(responseString);
        }
        try {
            final String requestEvent = json.getEvent();
            final Events event = Events.valueOf(requestEvent);
            final Agent agent = AgentMap.getAgentMap().getAgent(agentId);
            IEvents request;
            /*
             * Validating JSON format. Creating new thread for each request.
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
                            LOG.warn("Destination agent id not found(Hang up call");
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
                            LOG.warn("Destination agent id notfound(Bridge call)");
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
                            LOG.warn("Destination agent id notfound(Hand Over call)");
                            response = Response.status(Status.NO_CONTENT).build();
                        }
                        break;
                    default:
                        responseString = "action unidentified";
                        response = sendJsonResponse(responseString);

                }
            } else {
                LOG.warn("Destination agent id not found");
                responseString = "Agent not found";
                response = sendJsonResponse(responseString);
            }
        } catch (Exception e) {
            LOG.warn("Exception while processing event" + e);
        } finally {
            return response;
        }
    }

    private Response sendJsonResponse(final String responseString) {
        
        final OutputJson jsonOut = new OutputJson();
        jsonOut.setResponse(responseString);
        try {
            final String outStr = mapper.writeValueAsString(jsonOut);
            return Response.status(Status.FORBIDDEN).entity(outStr).build();
        } catch (Exception ex) {
            LOG.warn("Not able to write in output json class " + ex);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
