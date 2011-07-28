/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.asterisk.ami;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;

/**
 *
 * @author prashanth_p
 */
public class FinalState implements ManagerState {

    public void onPropertyChangeEvent(PropertyChangeEvent event, ManagerServer server) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        System.out.println("We are receiving the events after user, agent and supervisor are in conference");
    }

    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doParkCall(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redirectToConference(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void channelSpy(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ManagerState getInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
