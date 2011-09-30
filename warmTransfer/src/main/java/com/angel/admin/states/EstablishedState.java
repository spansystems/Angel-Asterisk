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
        LOG.info("Received onPropertyChange event " + getClass().toString());
        if (admin.getChannel() == (AsteriskChannel) event.getSource()) {
            LOG.info("Received a property change for admin channel");
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            if (channel.getState() == ChannelState.HUNGUP) {
                LOG.info("The admin state is set as hungup");
                admin.setChannelId(null);
                admin.setState(new InitialState());
                admin.setChannel(null);
            } else {
                LOG.info("Not implemented yet");
            }
        }
    }
}
