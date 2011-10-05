/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.agent.states.EstablishedState;
import com.angel.rest.InputJson;
import com.angel.utility.Actions;

public class UnParkEvent extends IEvents
{

	public UnParkEvent(final InputJson json, final Agent agent)
	{
		this.json = json;
		this.agent = agent;
	}

	@Override
	public void run()
	{
		if (agent.getState().toString().contains("HangupState") && null == agent.getChannel())
		{
			try
			{
				Actions.getActionObject().unParkUser(agent);
				LOG.info("User is unparked ..Now changing the state to established state");
				agent.setState(new EstablishedState());
			}

			catch (Exception e)
			{
				LOG.error("Not able to unpark the user", e);
			}
		}
	}
}
