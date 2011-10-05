package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.Agent;
import com.angel.base.ChannelOwner;
import com.angel.base.UserState;

public class InitialState extends UserState
{

	@Override
	public void onPropertyChangeEvent(final PropertyChangeEvent event, final Agent agent) throws IOException, TimeoutException
	{
		LOG.info("Received onPropertyChange event " + event.getSource().toString());
		final AsteriskChannel channel = (AsteriskChannel) event.getSource();
		handlePropertyChangeEvent(channel, agent);
	}

	private void processUserChannel(AsteriskChannel channel, Agent agent)
	{
		agent.getUser().setChannel(channel);
	}

	private void processAgentChannel(AsteriskChannel channel, Agent agent)
	{
		// TOdo : Handle all the state change events
		LOG.info("Received agent on property change event");
		agent.setChannel(channel);
		if (channel.getState() == ChannelState.UP)
		{
			LOG.info("Status of the system is established");
			LOG.info("The agent is now talking to user " + agent.getUser().getCallerId());
			agent.setState(new EstablishedState());
		}
		else
			if (channel.getState() == ChannelState.HUNGUP)
			{
				super.toInitialState(agent);

			}
	}

	private void handlePropertyChangeEvent(AsteriskChannel channel, Agent agent)
	{
		final ChannelOwner channelName = agent.getChannelOwner(channel);
		if (channelName.equals(ChannelOwner.USER))
		{
			LOG.info("User channel");
			processUserChannel(channel, agent);
		}
		else
			if (channelName.equals(ChannelOwner.AGENT))
			{
				LOG.info("Setting Agent channel");
				processAgentChannel(channel, agent);
			}
			else
			{
				LOG.info("Unknown channel");
			}
	}
}
