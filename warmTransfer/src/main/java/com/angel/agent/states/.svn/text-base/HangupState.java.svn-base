package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.Agent;
import com.angel.base.UserState;

public class HangupState extends UserState
{
	public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException,
			IOException, TimeoutException
	{
		LOG.info("Received onPropertyChange event in Hangup state " + event.getSource().toString());
		AsteriskChannel channel = (AsteriskChannel) event.getSource();
		if(channel.getState().equals(ChannelState.HUNGUP)){
			agent.setChannelId(null);// Mandatory to set to null
			agent.setChannel(null);
			agent.setAdmin(null);
		}
		
	}
}
