/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;
import com.angel.utility.Actions;

import org.asteriskjava.live.ChannelState;

public class ParkEvent extends IEvents
{

	public ParkEvent(final InputJson json1, final Agent agent)
	{
		this.json = json1;
		this.agent = agent;
	}

	@Override
	public void run()
	{
		try
		{
			if (agent.getState().toString().contains("EstablishedState") && agent.getChannel().getState() == ChannelState.UP)
			{
				Actions.getActionObject().parkCall(agent);
				LOG.info("Parked the user");
			}
			else
			{
				LOG.warn("Still the call is not established between user and agent");
			}
		}
		catch (Exception e)
		{
			LOG.error("Exception while agent trying to park user", e);
		}
	}
}
