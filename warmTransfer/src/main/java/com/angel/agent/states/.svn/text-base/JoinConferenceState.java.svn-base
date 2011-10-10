package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.RedirectAction;

import com.angel.agent.Agent;
import com.angel.base.ChannelOwner;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;

public class JoinConferenceState extends UserState
{

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException,
			TimeoutException
	{
		LOG.info("Event class received in JoinConference for " + event.getSource().getClass().toString());
		AsteriskChannel channel = (AsteriskChannel) event.getSource();
		ChannelOwner channelName = agent.getChannelOwner(channel);
		LOG.info("channel Name received is" + channelName);
		LOG.info("channel state for agent" + channel.getState().toString());
		if (channelName.equals(ChannelOwner.USER))
		{
			LOG.info("User channel received in Join conferene state", channel.getCallerId());
		}
		else
			if (channelName.equals(ChannelOwner.AGENT))
			{
				LOG.info("Agent channel received in Join conference state", channel.getCallerId());
			}

	}

	@Override
	public void redirectToConference(Agent agent)
	{
		LOG.info("Redirecting the parked channel to conference");
		RedirectAction action = new RedirectAction();
		action.setChannel(agent.getUser().getChannelName());
		action.setExtraChannel(agent.getChannel().getName());
		action.setExten("600");
		action.setPriority(1);
		action.setContext("meet");
		action.setExtraContext("meet");
		action.setExtraExten("600");
		action.setExtraPriority(1);

		try
		{
			ManagerServer.getManagerConnection().sendAction(action);
			agent.setState(new FinalState());
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (TimeoutException e)
		{
			e.printStackTrace();
		}
	}

	public void processUnparkEvent(Agent agent)
	{
		redirectToConference(agent);
		LOG.info("Redirecting user and agent to conference");
	}
}
