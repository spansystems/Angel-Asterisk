/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;


public class BridgeEvent extends IEvents implements IAsteriskEvent {

    public BridgeEvent(InputJson json1, Agent agent) {
        this.json = json1;
        this.agent = agent;
    }

    public void run() {
        if (agent.getState().toString().contains("ParkedCallState") && agent.getChannel() == null) {
           agent.getState().callToAdmin(json.getData().getDestinationAgent(), agent);
            //To be implemented
        }
    }
}
