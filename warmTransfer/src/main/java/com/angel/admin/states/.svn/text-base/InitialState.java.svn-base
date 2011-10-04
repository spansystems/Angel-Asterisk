package com.angel.admin.states;

import com.angel.agent.Admin;
import com.angel.agent.states.ParkedCallState;
import com.angel.base.UserState;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

/**
 * Initial state handler class.
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class InitialState extends UserState {

    @Override
    public void onPropertyChangeEvent(final PropertyChangeEvent event, final Admin admin) throws  IOException, TimeoutException {
        // TODO Auto-generated method stub
        LOG.info("Received onPropertyChange event for admin " + getClass().toString());
        if (event.getSource().getClass().toString().contains("AsteriskChannel")) {
            final AsteriskChannel channel = (AsteriskChannel) event.getSource();
            admin.setChannel(channel);
            if (channel.getState() == ChannelState.RINGING) {
                LOG.info("Adminchannel is ringing channel");
            } else if (channel.getState() == ChannelState.UP) {
                ((ParkedCallState) admin.getAgent().getState()).processAdminChannel(channel, admin.getAgent());
                admin.setState(new EstablishedState());
            } else {
                LOG.warn("unknown channel");
            }
        } else {
            LOG.warn("Unknown property change event");
        }
    }
}
