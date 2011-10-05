/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.manager;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.utility.AdminMap;
import com.angel.utility.AgentMap;

/**
 * 
 * @author prashanth_p
 */
public class AsteriskChannelEvents extends IManager implements AsteriskServerListener
{

	void initialize()
	{
		ManagerServer.getAsteriskServer().addAsteriskServerListener(this);
	}

	@Override
	public void onNewAsteriskChannel(final AsteriskChannel channel)
	{
		Logger.info("Asterisk Channel received: " + channel);
		final String channelId = channel.getId();
		final Agent userAgent = getUserAgentForTheChannel(channelId);
		if (userAgent != null)
		{
			userAgent.onNewAsteriskChannel(channel);
		}
		else
		{
			final Admin localAdmin = getAdminChannelID(channelId);
			if (null != localAdmin)
			{
				localAdmin.onNewAsteriskChannel(channel);
				Logger.info("In Admin new asterisk channel");
			}
			else
			{
				Logger.info("Asterisk channel Unidentified:" + channel);
			}
		}
	}

	@Override
	public void onNewMeetMeUser(MeetMeUser user)
	{
		Logger.info("Unsupported yet");
	}

	@Override
	public void onNewAgent(AsteriskAgentImpl agent)
	{
		Logger.info("Unsupported yet");
	}

	@Override
	public void onNewQueueEntry(AsteriskQueueEntry entry)
	{
		Logger.info("Unsupported yet");
	}

	/**
	 * Checks and returns the instance Agent(only) for Asterisk Channel event
	 * received.
	 * 
	 * @param id
	 *            of the agent.
	 * @return Agent the agent associated.
	 */

	public Agent getUserAgentForTheChannel(final String id)
	{
		if (null != AgentMap.getAgentMap().getAgentById(id))
		{
			return AgentMap.getAgentMap().getAgentById(id);
		}
		return null;
	}

	/**
	 * Returns admin instance according as the Asterisk channel received.
	 * 
	 * @param id
	 *            the admin id.
	 * @return Admin associated with id;
	 */
	public Admin getAdminChannelID(final String id)
	{
		return AdminMap.getAdminMap().getAdminById(id);
	}
}
