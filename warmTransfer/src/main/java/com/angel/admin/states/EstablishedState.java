package com.angel.admin.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

import com.angel.agent.Admin;
import com.angel.base.UserState;
import com.angel.utility.AdminMap;

/**
 * Established state handler.
 * 
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class EstablishedState extends UserState
{

	boolean userParked;

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException,
			IOException, TimeoutException
	{
		// TODO Auto-generated method stub
		LOG.info("Received onPropertyChange event in admin's established class" + getClass().toString());
		if (admin.getChannel() == (AsteriskChannel) event.getSource())
		{
			LOG.info("Received a property change for admin channel");
			final AsteriskChannel channel = (AsteriskChannel) event.getSource();
			if (channel.getState() == ChannelState.HUNGUP)
			{
				LOG.info("The admin state is set as hungup");
				admin.setChannelId(null);
				admin.setChannel(null);
				AdminMap.getAdminMap().removeAdmin(admin.getName());
				admin = null;// Make it null for garbage collection
				LOG.info("Removed admin object from Admin Map");
			}
			else
			{
				LOG.info("Not implemented yet");
			}
		}
	}
}
