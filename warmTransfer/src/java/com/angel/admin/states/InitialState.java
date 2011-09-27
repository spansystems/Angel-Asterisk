package com.angel.admin.states;

import com.angel.agent.Admin;
import com.angel.base.UserState;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

public class InitialState extends UserState {

    @Override
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
                ((com.angel.agent.states.ParkedCallState) admin.getAgent().getState()).processAdminChannel(channel, admin.getAgent());
                admin.setState(new EstablishedState());
            } else {
                System.out.println("Unknown channel");
            }
        } else {
            System.out.println("Unknown property change event");
        }
    }
}
