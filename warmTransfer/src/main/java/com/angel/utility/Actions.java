package com.angel.utility;

import java.io.IOException;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.ParkAction;
import org.asteriskjava.manager.action.ParkedCallsAction;
import org.asteriskjava.manager.action.RedirectAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.agent.states.FinalState;
import com.angel.base.IAgent;
import com.angel.manager.ManagerServer;

public class Actions
{
	static final Logger LOG = LoggerFactory.getLogger(Actions.class);
	private static Actions actions;

	public static Actions getActionObject()
	{
		if (null == actions)
		{
			actions = new Actions();
		}
		return actions;
	}

	public void parkCall(final Agent agent)
	{
		try
		{

			LOG.info("Trying to park call " + agent.getUser().getCallerId());
			ParkAction park = new ParkAction(agent.getUser().getChannelName(), agent.getChannel().getName());
			ManagerServer.getManagerConnection().sendAction(park);
			Thread.sleep(500);
			ParkedCallsAction parkAction = new ParkedCallsAction();
			ManagerServer.getManagerConnection().sendAction(parkAction);
			LOG.info("Sent the park action successfully");

		}
		catch (InterruptedException e)
		{
			LOG.warn("Park action interrupted");
		}
		catch (IllegalArgumentException e)
		{
			LOG.warn("Illegal argument exception");
		}
		catch (IllegalStateException e)
		{
			LOG.warn("illegal state exception");
		}
		catch (IOException e)
		{
			LOG.warn("IO exception while sending action msg");
		}
		catch (TimeoutException e)
		{
			LOG.warn("Timeout exception while sending action");
		}
	}

	public void hangupOtherEnd(final Agent agent)
	{
		try
		{
			Admin admin = agent.getAdmin();
			HangupAction hangup = new HangupAction();
			hangup.setChannel(admin.getChannel().getName());
			ManagerServer.getManagerConnection().sendAction(hangup);
			LOG.info("Sent hangup action successfully");
		}
		catch (IOException ex)
		{
			LOG.error("IOException in hangupOther end User", ex);
		}
		catch (TimeoutException ex)
		{
			LOG.error("Time out Exception in hangupOther end User", ex);
		}
		catch (IllegalArgumentException ex)
		{
			LOG.error("Illegal argument Exception in hangupOther end User", ex);
		}
		catch (IllegalStateException ex)
		{
			LOG.error("Illegal state Exception in hangupOther end User", ex);
		}
	}

	public void unParkUser(final Agent agent)
	{
		try
		{
			OriginateAction origin = new OriginateAction();
			LOG.info("Trying to unpark user with parking lot number Agent--->" + agent.getName() + "--->"
					+ agent.getUser().getParkingLotNo());
			String channel = "SIP/" + agent.getName() + "@" + ManagerServer.getOutboundproxy();
			origin.setChannel(channel);
			origin.setContext("pickuser");
			origin.setExten(agent.getUser().getParkingLotNo());
			origin.setPriority(1);
			origin.setCallerId(agent.getName());
			origin.setVariable("name", agent.getName());
			origin.setTimeout((long) 10000);
			ManagerServer.getManagerConnection().sendAction(origin);
			LOG.info("Sent the Un park action successfully");

		}
		catch (IOException ex)
		{
			LOG.error("IOException in pick User", ex);
		}
		catch (TimeoutException ex)
		{
			LOG.error("Time out Exception in pick User", ex);
		}
		catch (IllegalArgumentException ex)
		{
			LOG.error("Illegal argument Exception in pick User", ex);
		}
		catch (IllegalStateException ex)
		{
			LOG.error("Illegal state Exception in pick User", ex);
		}

	}

	public void cleanObject(IAgent agentAdmin)
	{
		if (agentAdmin instanceof Agent)
		{
			AgentMap.getAgentMap().removeAgent(agentAdmin.getName());
			LOG.info("Removing agent " + agentAdmin.getName() + " after hungup");
		}
		else
			if (agentAdmin instanceof Admin)
			{
				AdminMap.getAdminMap().removeAdmin(agentAdmin.getName());
				LOG.info("Removing admin " + agentAdmin.getName() + " after hungup");
			}

		agentAdmin = null;// Making it null for garbage collection.

	}

	@SuppressWarnings("deprecation")
	public void callToAdmin(final String destination, final Agent agent)
	{
		try
		{
			final OriginateAction origin = new OriginateAction();			
			String channel = "SIP/" + agent.getName() + "@" + ManagerServer.getOutboundproxy();
			origin.setChannel(channel);// Need to set the out bound IP
			origin.setContext("default");
			origin.setExten(destination);
			origin.setCallerId(agent.getName());
			origin.setPriority(Integer.valueOf(1));
			origin.setTimeout(Integer.valueOf(30000));
			ManagerServer.getManagerConnection().sendAction(origin);
			LOG.info("Sent call to admin action successfully");
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

	public void handOverCallToAdmin(final Agent agent, final String destinationAgent)
	{
		LOG.info("Redirecting the parked channel to conference");
		try
		{
			RedirectAction action = new RedirectAction();
			action.setChannel(agent.getAdmin().getChannel().getName());
			action.setExten(agent.getUser().getParkingLotNo());
			action.setPriority(1);
			action.setContext("pickuser");
			ManagerServer.getManagerConnection().sendAction(action);
			agent.setState(new FinalState());
		}
		catch (IllegalArgumentException e)
		{
			LOG.error("Illegal argument exception", e);
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			LOG.error("Illegal State exception ", e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			LOG.error("IO Exception", e);
			e.printStackTrace();
		}
		catch (TimeoutException e)
		{
			LOG.error("Time out Exception", e);
			e.printStackTrace();
		}
	}
}
