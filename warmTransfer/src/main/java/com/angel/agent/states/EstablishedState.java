package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.utility.Actions;

/**
 * User state handler.
 * 
 */
public class EstablishedState extends UserState
{

	private boolean userParked;

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException,
			IOException, TimeoutException
	{
		LOG.info("Received onPropertyChange event " + event.getSource().toString());
		final AsteriskChannel channel = (AsteriskChannel) event.getSource();
		if (channel.getState().equals(ChannelState.HUNGUP))
		{
			if (userParked == true)
			{
				processHanupAgent(channel, agent);
			}
			else
			{
				Actions.getActionObject().cleanObject(agent);
			}
		}
	}

	private void processHanupAgent(AsteriskChannel channel, Agent agent)
	{
		if (channel.getState().equals(ChannelState.HUNGUP))
		{
			LOG.info("The agents state is set as hungup");
			agent.setChannel(null);
			agent.setChannelId(null); // It's required to make it null ohterwise
										// the channel id's can be reused by
										// user.
			agent.setState(new HangupState());
		}
	}

	public void processParkedUser(boolean check)
	{
		userParked = check;
		LOG.info("User parked is ", check);
	}
}
