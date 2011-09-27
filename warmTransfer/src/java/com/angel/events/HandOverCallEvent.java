/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;


public class HandOverCallEvent extends IEvents implements IAsteriskEvent {

    public HandOverCallEvent(InputJson json, Agent agent) {
        this.json = json;
        this.agent = agent;
    }

    public void run() {
        //To be implemented
    }
}
