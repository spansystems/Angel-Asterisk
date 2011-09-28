/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.rest;

import java.io.Serializable;

public class OutputJson implements Serializable {
    private String agent;

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agentid) {
        this.agent = agentid;
    }
    

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    private String response;
}
