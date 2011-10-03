/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.rest;

import java.io.Serializable;

/**
 * Input json class.
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class InputJson implements Serializable {

    private static final long serialVersionUID = 1L;
    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(final String event) {
        this.event = event;
    }
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }

    /**
     * Data part of the json.
     */
    public static class Data implements Serializable {

        private static final long serialVersionUID = 1L;
        private String destinationAgent;
        private String agent;
        private String caller;

        public String getCaller() {
            return caller;
        }

        public void setCaller(final String caller) {
            this.caller = caller;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(final String agentId) {
            this.agent = agentId;
        }

        public String getDestinationAgent() {
            return destinationAgent;
        }

        public void setDestinationAgent(final String destinationAgentId) {
            this.destinationAgent = destinationAgentId;
        }
    }
}
