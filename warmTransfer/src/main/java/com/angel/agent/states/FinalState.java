/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent.states;

import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;

public class FinalState extends UserState {

    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        if (event.getSource().getClass().equals(agent.getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            if (channel.getState() == ChannelState.HUNGUP) {
                agent.setState(new InitialState());
            } else {
                LOG.warn("Unhandled yet");
            }
        }
    }
}
