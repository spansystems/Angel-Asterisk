/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;
import org.asteriskjava.live.ChannelState;


public class JoinCallEvent extends IEvents implements IAsteriskEvent {

    public JoinCallEvent(InputJson json1, Agent agent) {
        this.json = json1;
        this.agent = agent;
    }

    public void run() {
        if (agent.getState().toString().contains("TalkingToSuperVisorState") && agent.getAdmin().getChannel().getState() == ChannelState.UP) {
            agent.getState().redirectToConference(agent);
        }
        else{
            //Send response as agent not talking to destination agent
        }
    }
}
