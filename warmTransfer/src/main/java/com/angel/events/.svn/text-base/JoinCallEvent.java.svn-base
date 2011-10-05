/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;
import org.asteriskjava.live.ChannelState;

public class JoinCallEvent extends IEvents
{

	public JoinCallEvent(final InputJson json1, final Agent agent)
	{
		this.json = json1;
		this.agent = agent;
	}

	@Override
	public void run()
	{
		try
		{
			if (agent.getState().toString().contains("TalkingToSuperVisorState")
					&& agent.getAdmin().getChannel().getState() == ChannelState.UP)
			{
				agent.getState().redirectToConference(agent);
				LOG.info("Redirecting the admin to conference");
			}
			else
			{
				// Send response as agent not talking to destination agent
			}
		}
		catch (Exception e)
		{
			LOG.error("Exception while redirecting the admin to conference", e);
		}
	}
}
