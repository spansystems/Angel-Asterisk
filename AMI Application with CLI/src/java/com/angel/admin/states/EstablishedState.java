package com.angel.admin.states;

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

public class EstablishedState implements UserState {
    boolean userParked = false;

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException{
        // TODO Auto-generated method stub
        System.out.println("Received onPropertyChange event "
                + getClass().toString());        
        if (admin.getChannel() == (AsteriskChannel) event.getSource()) {
            System.out.println("Received a property change for admin channel");
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            if (channel.getState() == ChannelState.HUNGUP) {                
                System.out.println("The admin state is set as hungup");
                admin.setPeerChannel(null);
                admin.setState(new InitialState());
                admin.setChannel(null);
            }
            else {
                System.out.println("Unimplemented yet");
            }
        }
    }
    
    @Override
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        System.out.println("Received NewStateEvent event " + event + " "
                + getClass().toString());
        // TODO Auto-generated method stub

    }
    
    public void doParkCall(Agent agent) {
        try {
            
            
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
        } 
    }
    
    public void redirectToConference(ManagerServer managerServer) {
        // TODO Auto-generated method stub
        System.out.println("This event is not handled here :" + this);
    }
    
    public void channelSpy(ManagerServer managerServer) {
        // TODO Auto-generated method stub
        
    }
    
    public void setName() {
        // TODO Auto-generated method stub
        
    }
    
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public UserState getInstance() {
        // TODO Auto-generated method stub
        return this;
    }

    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void callToSu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redirectToConference(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  
}
