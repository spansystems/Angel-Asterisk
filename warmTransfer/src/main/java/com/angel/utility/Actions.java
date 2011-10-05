package com.angel.utility;

import java.io.IOException;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.ParkAction;
import org.asteriskjava.manager.action.ParkedCallsAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
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

	public void parkCall(Agent agent)
	{
		try
		{

			LOG.info("Trying to park call " + agent.getUser().getCallerId());
			final ParkAction park = new ParkAction(agent.getUser().getChannel().getName(), agent.getChannel().getName());
			ManagerServer.getManagerConnection().sendAction(park);
			Thread.sleep(500);
			final ParkedCallsAction parkAction = new ParkedCallsAction();
			ManagerServer.getManagerConnection().sendAction(parkAction);

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

	public void hangupOtherEnd(Agent agent)
	{
		try
		{
			Admin admin = agent.getAdmin();
			HangupAction hangup = new HangupAction();
			hangup.setChannel(admin.getChannel().getName());
			ManagerServer.getManagerConnection().sendAction(hangup);
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
	public void unParkUser(Agent agent)
	{
		try
		{
			OriginateAction origin = new OriginateAction();
			LOG.info("Trying to unpark user with parking lot number Agent--->" + agent.getName() + "--->"
					+ agent.getUser().getParkingLotNo());
			String channel = "SIP/" + agent.getName() + "@out";
			origin.setChannel(channel);
			origin.setContext("pickuser");
			origin.setExten(agent.getUser().getParkingLotNo());
			origin.setPriority(1);
			origin.setCallerId(agent.getName());
			origin.setVariable("name", agent.getName());
			origin.setTimeout((long) 10000);
			ManagerServer.getManagerConnection().sendAction(origin);
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
}
