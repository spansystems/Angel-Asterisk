package com.angel.base;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.apache.log4j.Logger;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;

public abstract class UserState {
    
    protected Logger logger = Logger.getLogger(UserState.class);
    
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
    }
    
    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
    }
    
    public void doParkCall(Agent agent) {
    }
    
    public void redirectToConference(ManagerServer managerServer) {
    }
    
    public void callToAdmin(String destination, Agent agent) {
        try {
            OriginateAction origin = new OriginateAction();
            origin = new OriginateAction();
            origin.setChannel("SIP/200@out");
            origin.setContext("default");
            origin.setExten(destination);
            origin.setCallerId("200");
            origin.setPriority(new Integer(1));
            origin.setTimeout(new Integer(30000));
            ManagerServer.getManagerConnection().sendAction(origin);
            logger.info("Sending the call to admin action");
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument exception", e);
        } catch (IllegalStateException e) {
            logger.error("Illegal State exception ", e);
        } catch (IOException e) {
            logger.error("IO Exception", e);
        } catch (TimeoutException e) {
            logger.error("Time out Exception", e);
        }
    }
    
    public void redirectToConference(Agent agent) {
    }
    
    public void hangupOtherEnd(Agent agent) {
    }
}
