/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent.states;
import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;

/**
 *
 * @author prashanth_p
 */
public class FinalState implements UserState {
 
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

    public UserState getInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        System.out.println("Unimplemented yet");
        if(event.getSource().getClass().equals(agent.getChannel().getClass())){
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            if(channel.getState() == ChannelState.HUNGUP){
                agent.setState(new InitialState());
            }
            else
                System.out.println("Unhandled yet");
        }
    }

    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        System.out.println("Not handled here");
    }

    public void doParkCall(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void callToSu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redirectToConference(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
