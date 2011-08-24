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

public class InitialState implements UserState {

    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        // TODO Auto-generated method stub
        System.out.println("Received onPropertyChange event for admin " + getClass().toString());
        if (event.getSource().getClass().toString().contains("AsteriskChannel")) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            admin.setChannel(channel);
            if (channel.getState() == ChannelState.RINGING) {
                System.out.println("Adminchannel is ringing channel");
            } else if (channel.getState() == ChannelState.UP) {
                ((com.angel.agent.states.ParkedCallState)admin.getAgent().getState()).processAdminChannel(channel, admin.getAgent());
                admin.setState(new EstablishedState());
            } else {
                System.out.println("Unknown channel");
            }
        } else {
            System.out.println("Unknown property change event");
        }
    }

    public void setName() {
        // TODO Auto-generated method stub
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserState getInstance() {
        // TODO Auto-generated method stub
        return this;
    }

    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doParkCall(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redirectToConference(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void channelSpy(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
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
