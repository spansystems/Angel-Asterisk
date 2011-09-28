package com.angel.admin.states;

import com.angel.agent.Admin;
import com.angel.base.UserState;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

public class EstablishedState extends UserState {

    boolean userParked = false;

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        // TODO Auto-generated method stub
        System.out.println("Received onPropertyChange event "
                + getClass().toString());
        if (admin.getChannel() == (AsteriskChannel) event.getSource()) {
            System.out.println("Received a property change for admin channel");
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            if (channel.getState() == ChannelState.HUNGUP) {
                System.out.println("The admin state is set as hungup");
                admin.setChannelId(null);
                admin.setState(new InitialState());
                admin.setChannel(null);
            } else {
                System.out.println("Unimplemented yet");
            }
        }
    }
}
