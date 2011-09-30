/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.manager;

import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.agent.User;

/**
 * 
 * @author prashanth_p
 */
public class ManagerEvents extends IManager implements ManagerEventListener
{

	public void run()
	{
		ManagerServer.getManagerConnection().addEventListener(this);
	}

	/*
	 * Receiving the manager events and filtering for new channel events.
	 * Storing the channel id's for user, agent and Admin. Corresponding events
	 * are sent to the respective handlers.
	 */
	@Override
	public void onManagerEvent(ManagerEvent event)
	{
		String eventType = event.getClass().toString();
		if (eventType.contains("NewChannelEvent"))
		{
			onNewChannelEvent(event);
		}
		else
			if (eventType.contains("NewCallerIdEvent"))
			{
				onNewCallerIdEvent(event);
				LOG.info("Received NewCallerIdEvent event " + event);
			}
			else
				if (eventType.contains("DialEvent"))
				{
					onDialEvent(event);
					// logger.info("Received Dial event " + event);
				}
				else
					if (eventType.contains("BridgeEvent"))
					{
						onBridgeEvent(event);
						LOG.info("Received Bridge Event " + event);
					}
					else
						if (eventType.contains("HangupEvent"))
						{
							onHangupEvent(event);
							LOG.info("Received Hangup Event " + event);
						}
						else
							if (eventType.contains("MeetMeJoinEvent"))
							{
								onMeetMeJoinEvent(event);
								LOG.info("Received MeetMeJoin Event " + event);
							}
							else
								if (eventType.contains("MeetMeLeaveEvent"))
								{
									onMeetMeLeaveEvent(event);
									LOG.info("Received MeetMeLeave Event " + event);
								}
								else
									if (eventType.contains("NewStateEvent"))
									{
										onNewStateEvent(event);
										LOG.info("Received NewState Event " + event);
									}
									else
										if (eventType.contains("ParkedCallEvent"))
										{
											onParkedCallEvent(event);
											LOG.info("Received ParkedCall Event " + event);
										}
										else
											if (eventType.contains("RegistryEvent"))
											{
												onRegistryEvent(event);
												LOG.info("Received Registry Event " + event);
											}
											else
												if (eventType.contains("OriginateSuccessEvent"))
												{
													onOriginateSuccessEvent(event);
													LOG.info("Received OriginateSuccess Event " + event);
												}
												else
												{
													LOG.info("The event received is not handled at this point and is:" + event);
												}
	}

	/*
	 * Receives the events from ManagerEvent method after getting filtered for
	 * new channel event It Initialized the channel id's for user,agent and
	 * superuser. These id's are used on New Asteisk channel event to recognize
	 * user admin or superuser.
	 */

	private void onNewChannelEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		NewChannelEvent channel = (NewChannelEvent) event;
		if (null != channel.getCallerIdName() && null != channel.getExten())
		{
			if (!AgentMap.checkAgentExist(channel.getExten()))
			{
				AgentMap.setAgent(channel.getExten(), new Agent(channel.getExten()));
			}
			setUserInfo(channel);
		}
		else
		{
			LOG.info("New Channel event received which is not of user:" + event);
		}
	}

	/*
	 * Handles dial event
	 */
	@SuppressWarnings("deprecation")
	private void onDialEvent(ManagerEvent event)
	{
		LOG.info("Dial channel event received: " + event);
		DialEvent channel = (DialEvent) event;
		if (null != channel.getDialString())
		{
			String dialedString = channel.getDialString().replace("@out", "");
			if (AgentMap.checkAgentExist(dialedString))
			{
				Agent agentReceived = AgentMap.getAgent(dialedString);
				agentReceived.setChannelId(channel.getDestUniqueId());
				LOG.info("Received an agent dial event " + agentReceived);
			}
			else
				if (AdminMap.checkAdminExist(dialedString) && AgentMap.checkAgentExist(channel.getCallerId()))
				{
					LOG.info("Admin channel event, setting the channel in the manager agent " + channel);
					Admin localadmin = AdminMap.getAdmin(dialedString);
					localadmin.setChannelId(channel.getDestUniqueId());
					AgentMap.getAgent(channel.getCallerId()).setChannelId(channel.getSrcUniqueId());
				}
				else
				{
					LOG.info("UnIdentified New CallerID event:" + event);
				}
		}
		else
		{
			LOG.info("Unknown dial event" + channel);
		}

	}

	private void onBridgeEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		BridgeEvent channel = (BridgeEvent) event;
		Agent agentWhoPickedUser = AgentMap.getAgent(channel.getCallerId1());
		if (null != agentWhoPickedUser && agentWhoPickedUser.getState().toString().contains("JoinConferenceState"))
		{
			agentWhoPickedUser.setChannelId(channel.getUniqueId1());
		}
	}

	@SuppressWarnings("deprecation")
	private void onHangupEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		HangupEvent channel = (HangupEvent) event;
		if (null != AgentMap.getAgentByUser(channel.getCallerId()))
		{
			// Agent agentToHangup =
			// AgentMap.getAgentByUser(channel.getCallerId());
			// To be implemented
		}

	}

	private void onMeetMeJoinEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
	}

	private void onMeetMeLeaveEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		// MeetMeLeaveEvent channel = (MeetMeLeaveEvent) event;

	}

	private void onNewStateEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		// NewStateEvent channel = (NewStateEvent) event;
	}

	/*
	 * Handles ParkedCallEvent. Controls the agents established state and is
	 * used to check whether user is parked or not.
	 */

	private void onParkedCallEvent(ManagerEvent event)
	{
		LOG.info("Parked  channel event received: " + event);
		ParkedCallEvent channel = (ParkedCallEvent) event;
		@SuppressWarnings("deprecation")
		String userCallerId = channel.getCallerId();
		Agent agentConnectedTo = AgentMap.getAgentByUser(userCallerId);
		if (null != agentConnectedTo)
		{
			((com.angel.agent.states.EstablishedState) agentConnectedTo.getState()).processParkedUser();
			agentConnectedTo.getUser().setParkingLotNo(channel.getExten());
		}
	}

	private void onRegistryEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		// RegistryEvent channel = (RegistryEvent) event;
	}

	private void onOriginateSuccessEvent(ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		// OriginateResponseEvent channel = (OriginateResponseEvent) event;
	}

	private void onNewCallerIdEvent(ManagerEvent event)
	{
		LOG.info("New caller Id event received " + event);
	}

	@SuppressWarnings("deprecation")
	private void setUserInfo(NewChannelEvent channel)
	{
		String extension = channel.getExten();
		if (AgentMap.checkAgentExist(extension))
		{
			Agent agentToContact = AgentMap.getAgent(extension);
			if (agentToContact.getUser() == null)
			{
				agentToContact.setUser(new User());
			}
			agentToContact.getUser().setChannelId(channel.getUniqueId());
			agentToContact.getUser().setCallerId(channel.getCallerId());
			LOG.info("Received the user manager channel event");
		}
		else
		{
			LOG.info("Agent, user trying to call doen't exist");
		}
	}
}
