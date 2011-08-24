package com.angel.base;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;

public interface UserState {

    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException;

    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException;

    public void onNewStateEvent(NewStateEvent event, ManagerServer server);

    public void doParkCall(Agent agent);

    public void redirectToConference(ManagerServer managerServer);
    
    public UserState getInstance();

    public void callToSu();

    public void redirectToConference(Agent agent);
  
}
