/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.rest;

import java.io.Serializable;

/**
 * Output json class.
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class OutputJson implements Serializable {

    private static final long serialVersionUID = 1L;
    private String agent;
    private String response;

    public String getAgent() {
        return agent;
    }

    public void setAgent(final String agentid) {
        this.agent = agentid;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(final String response) {
        this.response = response;
    }

}
