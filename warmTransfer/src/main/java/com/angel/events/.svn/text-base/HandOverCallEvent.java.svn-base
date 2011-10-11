/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.agent.states.TalkingToSuperVisorState;
import com.angel.rest.InputJson;
import com.angel.utility.Actions;

public class HandOverCallEvent extends IEvents
{

	public HandOverCallEvent(final InputJson json, final Agent agent)
	{
		this.json = json;
		this.agent = agent;
	}

	@Override
	public void run()
	{

		try
		{
			if (agent.getState() instanceof TalkingToSuperVisorState && null != agent.getChannel())
			{
				Actions.getActionObject().handOverCallToAdmin(agent, json.getData().getDestinationAgent());
				LOG.info("Received hand over call request.Processing it");
			}

			else
			{
				LOG.warn("Agent not talking to admin so can't handover the call");
			}
		}
		catch (Exception e)
		{
			LOG.error("Exception thrown in Hand over call event", e);
		}
	}
}
