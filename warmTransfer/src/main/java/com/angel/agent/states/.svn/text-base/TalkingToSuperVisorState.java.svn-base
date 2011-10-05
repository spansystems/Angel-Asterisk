package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.utility.Actions;

public class TalkingToSuperVisorState extends UserState
{

	/**
	 * 
	 * @param event
	 * @param agent
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws TimeoutException
	 */
	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException,
			IOException, TimeoutException
	{

		AsteriskChannel channel = (AsteriskChannel) event.getSource();
		LOG.info("Asterisk channel in Talking to su state " + channel);
		if (channel.getCallerId().toString().contains(agent.getName()) && channel.getState() == ChannelState.HUNGUP)
		{
			processAgentChannel(agent, channel);
		}

	}

	/**
	 * 
	 * @param agent
	 */
	@Override
	public void redirectToConference(Agent agent)
	{
		try
		{
			LOG.info("Redirect the SuperVisor channel to conference");
			agent.getAdmin().getChannel().redirect("meet", "600", 1);
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
		}
		catch (ManagerCommunicationException e)
		{
			// TODO Auto-generated catch block
		}
		catch (NoSuchChannelException e)
		{
			// TODO Auto-generated catch block
		}
	}

	private void processAgentChannel(Agent agent, AsteriskChannel channel)
	{
		if (channel.getState() == ChannelState.HUNGUP)
		{
			LOG.info("The agent's channel is hungup after putting admin's channel in confernence");
			agent.setChannel(null);
			agent.setChannelId(null);// Required to make it null
			try
			{
				Actions.getActionObject().unParkUser(agent);
				agent.setState(new JoinConferenceState());
				LOG.info("Changing the state to join conference state");
			}

			catch (Exception e)
			{
				LOG.error("Not able to unpark the user in talking to supervisor state",e);
			}

		}
	}
}
