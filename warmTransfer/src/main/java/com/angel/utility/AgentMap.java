/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.utility;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.angel.agent.Agent;

/**
 * Agent map class.
 * 
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class AgentMap
{
	private static final Logger LOGGER = Logger.getLogger(AgentMap.class);
	private static AgentMap agentMap;
	private Map<String, Agent> agentHashMap = new ConcurrentHashMap<String, Agent>();

	/**
	 * Default constructor.
	 */
	private AgentMap()
	{

	}

	/**
	 * getter for agent map.
	 * 
	 * @return the agent map object.
	 */
	public static AgentMap getAgentMap()
	{
		if (agentMap == null)
		{
			agentMap = new AgentMap();
		}
		return agentMap;
	}

	/**
	 * sets the agent with corrsponding id.
	 * 
	 * @param id
	 *            the agent id.
	 * @param agent
	 *            the agent object.
	 */
	public void setAgent(final String id, final Agent agent)
	{
		agentHashMap.put(id, agent);
	}

	/**
	 * Returns the agent object associated with the key.
	 * 
	 * @param key
	 *            the agent key.
	 * @return the agent object.
	 */
	public Agent getAgent(final String key)
	{
		return agentHashMap.get(key);
	}

	/**
	 * Removes the agent from the map.
	 * 
	 * @param key
	 *            the agent key.
	 */
	public void removeAgent(final String key)
	{
		agentHashMap.remove(key);
	}

	/**
	 * Checks whether the agent exist in the hash map.
	 * 
	 * @param agent
	 *            the agent id of the hash map.
	 * @return boolean stating success or failure.
	 */
	public boolean checkAgentExist(final String agent)
	{
		return agentHashMap.containsKey(agent);
	}

	/**
	 * Returns Agent object by channel id.
	 * 
	 * @param id
	 *            The agent id associated.
	 * @return Agent the agent associated.
	 */
	public Agent getAgentById(final String id)
	{
		final Collection<Agent> c = agentHashMap.values();
		final Iterator<Agent> it = c.iterator();
		try
		{
			while (it.hasNext())
			{
				Agent agent = (Agent) it.next();
				if (agent.getUser().getChannelId().equals(id) || agent.getChannelId().equals(id))
				{
					LOGGER.info("Success returned from getAgent from ID");
					return agent;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.warn("Exception while checking agent Id or User id",e);
		}
		LOGGER.info("Not Success");
		return null;
	}

	/**
	 * Returns Agent object by User/Caller id.
	 * 
	 * @param userCallerId
	 *            the caller id of the user.
	 * @return Agent associated with the caller.
	 */
	public Agent getAgentByUser(final String userCallerId)
	{
		final Collection<Agent> c = agentHashMap.values();
		final Iterator<Agent> it = c.iterator();
		while (it.hasNext())
		{
			final Agent agent = (Agent) it.next();
			if (agent.getUser().getCallerId().equals(userCallerId))
			{
				LOGGER.info("Success returned from getAgent from ID");
				return agent;
			}
		}
		LOGGER.info("Not Success");
		return null;
	}
}
