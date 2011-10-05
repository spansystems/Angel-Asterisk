/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.events;

import com.angel.agent.Agent;
import com.angel.agent.states.HangupState;
import com.angel.rest.InputJson;
import com.angel.utility.Actions;
import com.angel.utility.AgentMap;

public class HangUpEvent extends IEvents
{

	public HangUpEvent(final InputJson json1, final Agent agent)
	{
		this.json = json1;
		this.agent = agent;
	}

	@Override
	public void run()
	{
		String agentId = json.getData().getAgent();
		agent = AgentMap.getAgentMap().getAgent(agentId);
		try
		{
			if (null != agent)
			{
				if (agent.getState().toString().contains("TalkingToSuperVisorState") && null != agent.getChannel())
				{
					Actions.getActionObject().hangupOtherEnd(agent);
					agent.setState(new HangupState());
				}
				else
				{
					// Send response as Agent not talking to Agent2/admin
				}
			}
			else
			{
				// Send response as Agent not logged in
			}
		}
		catch (Exception e)
		{

		}
	}
}
