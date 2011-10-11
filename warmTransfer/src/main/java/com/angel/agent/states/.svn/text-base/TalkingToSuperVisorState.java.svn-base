package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.RedirectAction;

import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;

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
		if (channel.getCallerId().toString().contains(agent.getName()) && channel.getState().equals(ChannelState.HUNGUP))
		{
			LOG.info("Agent channel is down");
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
			/*
			 * Redirecting the admin/agent2 to conference
			 */

			RedirectAction action = new RedirectAction();
			action.setChannel(agent.getAdmin().getChannel().getName());
			action.setExten("600");
			action.setPriority(1);
			action.setContext("meet");
			/*
			 * Redirecting the agent to pick up user channel
			 */
			action.setExtraChannel(agent.getChannel().getName());
			LOG.info("User channel is :", agent.getChannel().getName());
			action.setExtraContext("pickuser");
			action.setExtraExten(agent.getUser().getParkingLotNo());
			action.setExtraPriority(1);

			ManagerServer.getManagerConnection().sendAction(action);

			// Changing the state to join conference state
			agent.setState(new JoinConferenceState());
			LOG.info("Changing the state to join conference state");
		}
		catch (IllegalArgumentException e)
		{
			LOG.error("Illegal argument Exception while sending redirect action ", e);
		}
		catch (IllegalStateException e)
		{
			LOG.error("Illegal state Exception while sending redirect action ", e);
		}
		catch (ManagerCommunicationException e)
		{
			LOG.error("Manager communication Exception while sending redirect action ", e);
		}
		catch (NoSuchChannelException e)
		{
			LOG.error("No such channel Exception while sending redirect action ", e);
		}
		catch (IOException e)
		{
			LOG.error("IO Exception while sending redirect action ", e);
			e.printStackTrace();
		}
		catch (TimeoutException e)
		{
			LOG.error("Time out Exception while sending redirect action ", e);
			e.printStackTrace();
		}
	}
}
