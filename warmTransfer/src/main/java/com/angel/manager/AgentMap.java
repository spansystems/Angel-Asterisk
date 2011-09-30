/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.angel.agent.Agent;

public class AgentMap
{
	static final Logger logger = Logger.getLogger(AgentMap.class);

	public static void setAgent(String customer, Agent agent)
	{
		agentMap.put(customer, agent);
	}

	public static Agent getAgent(String key)
	{
		if (agentMap.containsKey(key))
		{
			return agentMap.get(key);
		}
		else
		{
			return null;
		}
	}

	public static void removeAgent(String key)
	{
		agentMap.remove(key);
	}

	private static Map<String, Agent> agentMap = new ConcurrentHashMap<String, Agent>();

	static boolean checkAgentExist(String agent)
	{
		if (agentMap.containsKey(agent))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * Returns Agent object by channel id
	 */

	static Agent getAgentById(String id)
	{
		Collection<Agent> c = agentMap.values();
		Iterator<Agent> it = c.iterator();
		while (it.hasNext())
		{
			Agent agent = (Agent) it.next();
			if (agent.getChannelId().equals(id) || agent.getUser().getChannelId().equals(id))
			{
				logger.info("Success returned from getAgent from ID");
				return agent;
			}
		}
		logger.info("Not Success");
		return null;
	}

	/*
	 * Returns Agent object by User/Caller id.
	 */

	static Agent getAgentByUser(String userCallerId)
	{
		Collection<Agent> c = agentMap.values();
		Iterator<Agent> it = c.iterator();
		while (it.hasNext())
		{
			Agent agent = (Agent) it.next();
			if (agent.getUser().getCallerId().equals(userCallerId))
			{
				logger.info("Success returned from getAgent from ID");
				return agent;
			}
		}
		logger.info("Not Success");
		return null;
	}
}
