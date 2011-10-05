package com.angel.base;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.manager.ManagerServer;
import com.angel.utility.AgentMap;

public abstract class UserState
{

	public static final Logger LOG = LoggerFactory.getLogger(UserState.class);

	public void onPropertyChangeEvent(final PropertyChangeEvent event, final Agent agent) throws IllegalArgumentException,
			IllegalStateException, IOException, TimeoutException
	{
	}

	public void onPropertyChangeEvent(final PropertyChangeEvent event, final Admin admin) throws IllegalArgumentException,
			IllegalStateException, IOException, TimeoutException
	{
	}

	@SuppressWarnings("deprecation")
	public void callToAdmin(final String destination, final Agent agent)
	{
		try
		{
			OriginateAction origin = new OriginateAction();
			origin = new OriginateAction();
			origin.setChannel("SIP/200@out");
			origin.setContext("default");
			origin.setExten(destination);
			origin.setCallerId(agent.getName());
			origin.setPriority(new Integer(1));
			origin.setTimeout(new Integer(30000));
			ManagerServer.getManagerConnection().sendAction(origin);
			LOG.info("Sending the call to admin action");
		}
		catch (IllegalArgumentException e)
		{
			LOG.error("Illegal argument exception", e);
		}
		catch (IllegalStateException e)
		{
			LOG.error("Illegal State exception ", e);
		}
		catch (IOException e)
		{
			LOG.error("IO Exception", e);
		}
		catch (TimeoutException e)
		{
			LOG.error("Time out Exception", e);
		}
	}

	public void redirectToConference(final Agent agent)
	{
	}	

	public void toInitialState(final Agent agent)
	{
		agent.setChannel(null);
		agent.setChannelId(null);
		agent.setUser(null);
		AgentMap.getAgentMap().removeAgent(agent.name);
		LOG.info("Removin agent after hungup");
	}
}