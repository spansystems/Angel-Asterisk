/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;

public class UnParkEvent extends IEvents {

    public UnParkEvent(final InputJson json, final Agent agent) {
        this.json = json;
        this.agent = agent;
    }

    @Override
    public void run() {
        //To be implemented
    }
}
