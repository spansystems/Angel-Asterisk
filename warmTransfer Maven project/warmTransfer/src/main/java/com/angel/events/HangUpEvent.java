/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;


public class HangUpEvent extends IEvents implements IAsteriskEvent {

    public HangUpEvent(InputJson json1, Agent agent) {
        this.json = json1;
        this.agent = agent;
    }

    public void run() {
//        String agentId = json.getData().getAgent();
//        agent = AgentMap.getAgent(agentId);
//        if (null != agent) {
//            if (agent.getState().toString().contains("TalkingToSuperVisorState") && agent.getChannel() != null) {
//                ((com.angel.agent.states.TalkingToSuperVisorState) agent.getState()).hangupOtherEnd(agent);
//            } else {
//                //Send response as Agent not talking to Agent2/admin
//            }
//        } else {
//            //Send response as Agent not logged in
//        }
        //To be implemented
    }
}
