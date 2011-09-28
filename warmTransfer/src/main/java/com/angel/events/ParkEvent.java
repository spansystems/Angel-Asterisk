/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;
import org.asteriskjava.live.ChannelState;


public class ParkEvent extends IEvents implements IAsteriskEvent {

    public ParkEvent(InputJson json1, Agent agent) {
        this.json = json1;
        this.agent = agent;
    }

    @Override
    public void run() {
        if (agent.getState().toString().contains("EstablishedState") && agent.getChannel().getState() == ChannelState.UP) {
            agent.getState().doParkCall(agent);
        } else {
            //Send the response stating call not established b/w user and agent.
        }
    }
}
