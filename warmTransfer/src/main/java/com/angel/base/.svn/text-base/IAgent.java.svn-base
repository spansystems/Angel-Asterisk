/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.base;

import org.asteriskjava.live.AsteriskChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IAgent
{

	protected static final Logger LOG = LoggerFactory.getLogger(IAgent.class);
	protected String name;
	protected AsteriskChannel channel;
	protected UserState state;

	public void setState(UserState state)
	{
		this.state = state;
	}

	public UserState getState()
	{
		return state;
	}

	public AsteriskChannel getChannel()
	{
		return channel;
	}

	public void setChannel(AsteriskChannel channel)
	{
		this.channel = channel;
	}

	public String getName()
	{
		return name;
	}

	protected void onNewAsteriskChannel(AsteriskChannel channel)
	{
		LOG.info("New asterisk channel received in abstract class IAgent", channel);
	}
}
