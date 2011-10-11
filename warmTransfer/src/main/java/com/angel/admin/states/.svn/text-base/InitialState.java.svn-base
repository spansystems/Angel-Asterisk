package com.angel.admin.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.Admin;
import com.angel.agent.states.TalkingToSuperVisorState;
import com.angel.base.UserState;
import com.angel.utility.Actions;

/**
 * Initial state handler class.
 * 
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class InitialState extends UserState
{

	@Override
	public void onPropertyChangeEvent(final PropertyChangeEvent event, final Admin admin) throws IOException, TimeoutException
	{
		LOG.info("Received onPropertyChange event for admin " + getClass().toString());
		final AsteriskChannel channel = (AsteriskChannel) event.getSource();
		admin.setChannel(channel);

		if (channel.getState().equals(ChannelState.UP))
		{
			admin.getAgent().setState(new TalkingToSuperVisorState());
			admin.setState(new EstablishedState());
		}
		else
			if (channel.getState().equals(ChannelState.HUNGUP))
			{
				LOG.info("Received hungup channel for admin " + admin.getName() + " in initial state");
				Actions.getActionObject().cleanObject(admin);
			}
			else
			{
				LOG.warn("Unknown channel in Admin's" + admin.getName() + " initial state");
			}
	}
}
