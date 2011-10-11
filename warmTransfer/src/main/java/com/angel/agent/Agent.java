/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.agent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.states.InitialState;
import com.angel.base.ChannelOwner;
import com.angel.base.IAgent;
import com.angel.base.UserState;

/**
 * Agent class.
 * 
 */
public class Agent extends IAgent implements PropertyChangeListener
{

	private Admin admin;
	private String channelId;
	private User user;

	public Agent(final String name)
	{
		this.state = new InitialState();
		this.name = name;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}

	public User getUser()
	{
		return user;
	}

	/**
	 * Used to return the Channel ID
	 * 
	 * @return
	 */
	public String getChannelId()
	{
		return channelId;
	}

	/**
	 * 
	 * @param channelId
	 */
	public void setChannelId(final String channelId)
	{
		this.channelId = channelId;
	}

	/**
	 * 
	 * @return
	 */
	public Admin getAdmin()
	{
		return admin;
	}

	/**
	 * 
	 * @param admin
	 */
	public void setAdmin(Admin admin)
	{
		this.admin = admin;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public AsteriskChannel getChannel()
	{
		return channel;
	}

	/**
	 * 
	 * @param channel
	 */
	@Override
	public void setChannel(final AsteriskChannel channel)
	{
		this.channel = channel;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public UserState getState()
	{
		return this.state;
	}

	/**
	 * 
	 * @param state
	 */
	@Override
	public void setState(final UserState state)
	{
		this.state = state;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt)
	{
		try
		{
			getState().onPropertyChangeEvent(evt, this);
		}
		catch (IllegalArgumentException ex)
		{
			LOG.error("Illegal argument exception ", ex);
		}
		catch (IllegalStateException ex)
		{
			LOG.error("Illegal state exception ", ex);
		}
		catch (IOException ex)
		{
			LOG.error("IO exception ", ex);
		}
		catch (TimeoutException ex)
		{
			LOG.error("Time out exception ", ex);
		}
	}

	/**
	 * 
	 * @param receivedChannel
	 * @return
	 */
	public ChannelOwner getChannelOwner(final AsteriskChannel receivedChannel)
	{
		LOG.info("Inside get channel owner in getchannel owner");
		if (receivedChannel.getId().equals(getChannelId()))
		{
			return ChannelOwner.AGENT;
		}
		else
			if (receivedChannel.getId().equals(user.getChannelId()))
			{
				return ChannelOwner.USER;
			}
			else
			{
				return ChannelOwner.UNKNOWN;
			}
	}

	/**
	 * 
	 * @param channel
	 */
	@Override
	public void onNewAsteriskChannel(final AsteriskChannel channel)
	{
		if (channel.getId().equals(getChannelId()))
		{
			setChannel(channel);
			channel.addPropertyChangeListener(this);
		}
		else
			if (channel.getId().equals(user.getChannelId()))
			{
				if (!channel.getName().contains("Parked"))
				{
					user.setChannel(channel);
					user.setChannelName(channel.getName());
					LOG.info("User channel set is :" + channel);
				}
				channel.addPropertyChangeListener(this);
			}
			else
			{
				LOG.info("Unknown channel event");
			}
	}
}
