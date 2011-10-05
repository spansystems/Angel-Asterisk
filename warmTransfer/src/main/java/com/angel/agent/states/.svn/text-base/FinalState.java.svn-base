/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.Agent;
import com.angel.base.UserState;

public class FinalState extends UserState {

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException,
          IOException, TimeoutException {
        AsteriskChannel channel = (AsteriskChannel) event.getSource();
        if (channel.getState() == ChannelState.HUNGUP) {
        	super.toInitialState(agent);
        } else {
            LOG.info("Unhandled yet");
        }

    }
}
