/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.rest;

import java.io.Serializable;


public class InputJson implements Serializable {

    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable {

        private String destinationAgent;
        private String agent;
        private String caller;

        public String getCaller() {
            return caller;
        }

        public void setCaller(String caller) {
            this.caller = caller;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agentId) {
            this.agent = agentId;
        }

        public String getDestinationAgent() {
            return destinationAgent;
        }

        public void setDestinationAgent(String destinationAgentId) {
            this.destinationAgent = destinationAgentId;
        }
    }
}
