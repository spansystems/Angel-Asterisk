/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.events;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.agent.states.HangupState;
import com.angel.rest.InputJson;
import com.angel.utility.Actions;
import com.angel.utility.AdminMap;

public class BridgeEvent extends IEvents
{

	public BridgeEvent(final InputJson json1, final Agent agent)
	{
		this.json = json1;
		this.agent = agent;
	}

	@Override
	public void run()
	{
		try
		{
			if (agent.getState() instanceof HangupState && agent.getChannel() == null)
			{
				final String adminName = json.getData().getDestinationAgent();
				if (!AdminMap.getAdminMap().checkAdminExist(adminName))
				{
					Admin admin = new Admin(adminName);
					AdminMap.getAdminMap().setAdmin(adminName, admin);
					agent.setAdmin(admin);
					admin.setAgent(agent);
					Actions.getActionObject().callToAdmin(json.getData().getDestinationAgent(), agent);
				}
				else
				{
					LOG.warn("Admin already exists and in call", adminName);
				}

			}
			else
			{
				LOG.error("Not able to call to Admin");
			}
		}
		catch (Exception e)
		{
			LOG.error("Exception excuting the bridge call event", e);
		}
	}
}
