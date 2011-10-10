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
import org.asteriskjava.manager.event.NewStateEvent;

import com.angel.admin.states.InitialState;
import com.angel.base.IAgent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;

/**
 * Admin instance of the class.
 * 
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public final class Admin extends IAgent implements PropertyChangeListener
{

	private Agent agent;
	private String channelId;

	public Admin(String name)
	{
		this.state = new InitialState();
		this.name = name;
	}

	public String getChannelId()
	{
		return channelId;
	}

	/**
	 * 
	 * @param channelId
	 */
	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	/**
	 * 
	 * @return
	 */
	public Agent getAgent()
	{
		return agent;
	}

	/**
	 * 
	 * @param agent
	 */
	public void setAgent(Agent agent)
	{
		this.agent = agent;
	}

	/**
     * 
     */

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
	public void setChannel(AsteriskChannel channel)
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
		return state;
	}

	/**
	 * 
	 * @param state
	 */
	@Override
	public void setState(UserState state)
	{
		this.state = state;
	}

	/**
	 * 
	 * @param event
	 * @param server
	 */
	public void onNewStateEvent(NewStateEvent event, ManagerServer server)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * 
	 * @return
	 */
	public Admin getInstance()
	{
		return this;
	}

	/**
	 * 
	 * @param channel
	 */
	@Override
	public void onNewAsteriskChannel(AsteriskChannel channel)
	{
		setChannel(channel);
		channel.addPropertyChangeListener(this);
		LOG.info("Received the admin channel " + channel);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		try
		{
			state.onPropertyChangeEvent(evt, this);
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
}
