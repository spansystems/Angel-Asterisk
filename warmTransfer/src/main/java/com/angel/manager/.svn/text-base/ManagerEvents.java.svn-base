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
import org.asteriskjava.manager.event.MeetMeJoinEvent;
import org.asteriskjava.manager.event.MeetMeLeaveEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateSuccessEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;
import org.asteriskjava.manager.event.RegistryEvent;
import org.asteriskjava.manager.event.UnparkedCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.agent.User;
import com.angel.agent.states.EstablishedState;
import com.angel.agent.states.JoinConferenceState;
import com.angel.utility.Actions;
import com.angel.utility.AdminMap;
import com.angel.utility.AgentMap;

/**
 * Manager event listener.
 * 
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
@SuppressWarnings("deprecation")
public class ManagerEvents extends IManager implements ManagerEventListener
{
	private static final Logger LOG = LoggerFactory.getLogger(ManagerEvents.class);

	/**
	 * Initializes manager interfaces.
	 */
	public void initialize()
	{
		ManagerServer.getManagerConnection().addEventListener(this);
	}

	/*
	 * Receiving the manager events and filtering for new channel events.
	 * Storing the channel id's for user, agent and Admin. Corresponding events
	 * are sent to the respective handlers.
	 */
	@Override
	public void onManagerEvent(final ManagerEvent event)
	{
		// To do; Use switch instead of if-else ladder

		if (event instanceof NewChannelEvent)
		{
			onNewChannelEvent(event);
		}
		else
			if (event instanceof NewCallerIdEvent)
			{
				onNewCallerIdEvent(event);
				LOG.info("Received NewCallerIdEvent event " + event);
			}
			else
				if (event instanceof DialEvent)
				{
					onDialEvent(event);
				}
				else
					if (event instanceof BridgeEvent)
					{
						onBridgeEvent(event);
						LOG.info("Received Bridge Event " + event);
					}
					else
						if (event instanceof HangupEvent)
						{
							onHangupEvent(event);
							LOG.info("Received Hangup Event " + event);
						}
						else
							if (event instanceof MeetMeJoinEvent)
							{
								onMeetMeJoinEvent(event);
								LOG.info("Received MeetMeJoin Event " + event);
							}
							else
								if (event instanceof MeetMeLeaveEvent)
								{
									onMeetMeLeaveEvent(event);
									LOG.info("Received MeetMeLeave Event " + event);
								}
								else
									if (event instanceof NewStateEvent)
									{
										onNewStateEvent(event);
										LOG.info("Received NewState Event " + event);
									}
									else
										if (event instanceof ParkedCallEvent)
										{
											onParkedCallEvent(event);
											LOG.info("Received ParkedCall Event " + event);
										}
										else
											if (event instanceof RegistryEvent)
											{
												onRegistryEvent(event);
												LOG.info("Received Registry Event " + event);
											}
											else
												if (event instanceof OriginateSuccessEvent)
												{
													onOriginateSuccessEvent(event);
													LOG.info("Received OriginateSuccess Event " + event);
												}
												else
													if (event instanceof UnparkedCallEvent)
													{
														onUnparkedCallEvent(event);
														System.out.println("Received NewCallerIdEvent event " + event);
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
	private void onUnparkedCallEvent(final ManagerEvent event)
	{
		LOG.info("Unparked call event received");
		final UnparkedCallEvent unParkEvent = (UnparkedCallEvent) event;
		if (null != AgentMap.getAgentMap().getAgentByUser(unParkEvent.getCallerId()))
		{
			final Agent unParkedUsersAgent = AgentMap.getAgentMap().getAgentByUser(unParkEvent.getCallerId());
			unParkedUsersAgent.getUser().setChannelName(unParkEvent.getChannel());
			if (unParkedUsersAgent.getState() instanceof EstablishedState)
			{
				((com.angel.agent.states.EstablishedState) unParkedUsersAgent.getState()).processParkedUser(false);
			}
			else
				if (unParkedUsersAgent.getState() instanceof JoinConferenceState)
				{
					LOG.info("User unpark event received in talking to supervisor state");
					((com.angel.agent.states.JoinConferenceState) unParkedUsersAgent.getState()).processUnparkEvent(unParkedUsersAgent);
				}
		}
		else
		{
			LOG.warn("Unknow unpark event received");
		}

	}

	private void onNewChannelEvent(final ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		NewChannelEvent channel = (NewChannelEvent) event;
		if (null != channel.getCallerId() && null != channel.getExten())
		{
			if (!AgentMap.getAgentMap().checkAgentExist(channel.getExten()))
			{
				AgentMap.getAgentMap().setAgent(channel.getExten(), new Agent(channel.getExten()));
				LOG.info("Added new agent to Agent Map", channel.getExten());
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
	private void onDialEvent(final ManagerEvent event)
	{
		LOG.info("Dial channel event received: " + event);
		final DialEvent channel = (DialEvent) event;
		if (null != channel.getDialString())
		{

			final String[] dialedStringArray = channel.getDialString().split("@");
			final String dialedString = dialedStringArray[0];
			if (AgentMap.getAgentMap().checkAgentExist(dialedString))
			{
				final Agent agentReceived = AgentMap.getAgentMap().getAgent(dialedString);
				agentReceived.setChannelId(channel.getDestUniqueId());
				LOG.info("Received an agent dial event " + agentReceived);
			}
			else
				if (AdminMap.getAdminMap().checkAdminExist(dialedString) && AgentMap.getAgentMap().checkAgentExist(channel.getCallerId()))
				{
					LOG.info("Admin channel event, setting the channel in the manager agent " + channel);
					final Admin localadmin = AdminMap.getAdminMap().getAdmin(dialedString);
					localadmin.setChannelId(channel.getDestUniqueId());
					AgentMap.getAgentMap().getAgent(channel.getCallerId()).setChannelId(channel.getSrcUniqueId());
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

	private void onBridgeEvent(final ManagerEvent event)
	{
		LOG.info("Bridge event received: " + event);
		final BridgeEvent channel = (BridgeEvent) event;
		final String dialedString = channel.getCallerId2();
		try
		{
			if (AgentMap.getAgentMap().checkAgentExist(dialedString))
			{
				final Agent agentReceived = AgentMap.getAgentMap().getAgent(dialedString);
				agentReceived.setChannelId(channel.getUniqueId2());
				LOG.info("Received an agent bride event " + agentReceived);
			}
			else
			{
				if (AgentMap.getAgentMap().checkAgentExist(channel.getCallerId1()))
				{
					if (AdminMap.getAdminMap().checkAdminExist(dialedString))
					{
						LOG.info("Received admin---->", dialedString, channel);
						final Admin localadmin = AdminMap.getAdminMap().getAdmin(dialedString);
						localadmin.setChannelId(channel.getUniqueId2());
					}
					LOG.info("Setting Agent channel ID for agent--->", channel.getCallerId1());
					AgentMap.getAgentMap().getAgent(channel.getCallerId1()).setChannelId(channel.getUniqueId1());

				}
				else
				{
					LOG.info("UnIdentified Bridge event:" + event);
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("Caught exception in Bridge event handler", e);
		}

	}

	private void onHangupEvent(final ManagerEvent event)
	{
		LOG.info("Manager Hangup channel event received: " + event);
		HangupEvent channel = (HangupEvent) event;
		if (null != AgentMap.getAgentMap().getAgentByUser(channel.getCallerId()))
		{
			Agent agentToHangup = AgentMap.getAgentMap().getAgentByUser(channel.getCallerId());
			LOG.info("Removing agent from agent map", agentToHangup.getName());
			Actions.getActionObject().cleanObject(agentToHangup);

		}
		if (null != AdminMap.getAdminMap().getAdminById(channel.getCallerId()))
		{
			Admin adminHungup = AdminMap.getAdminMap().getAdminById(channel.getCallerId());
			LOG.info("Removing agent from agent map", adminHungup.getName());
			Actions.getActionObject().cleanObject(adminHungup);
		}

	}

	private void onMeetMeJoinEvent(final ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
	}

	private void onMeetMeLeaveEvent(final ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		// MeetMeLeaveEvent channel = (MeetMeLeaveEvent) event;

	}

	private void onNewStateEvent(final ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
		// NewStateEvent channel = (NewStateEvent) event;
	}

	/*
	 * Handles ParkedCallEvent. Controls the agents established state and is
	 * used to check whether user is parked or not.
	 */
	private void onParkedCallEvent(final ManagerEvent event)
	{
		LOG.info("Parked  channel event received: " + event);
		final ParkedCallEvent channel = (ParkedCallEvent) event;
		final String userCallerId = channel.getCallerId();
		final Agent agentConnectedTo = AgentMap.getAgentMap().getAgentByUser(userCallerId);
		if (null != agentConnectedTo)
		{
			((com.angel.agent.states.EstablishedState) agentConnectedTo.getState()).processParkedUser(true);
			agentConnectedTo.getUser().setParkingLotNo(channel.getExten());
		}
	}

	private void onRegistryEvent(final ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
	}

	private void onOriginateSuccessEvent(final ManagerEvent event)
	{
		LOG.info("Manager new channel event received: " + event);
	}

	private void onNewCallerIdEvent(final ManagerEvent event)
	{
		LOG.info("New caller Id event received " + event);
	}

	private void setUserInfo(final NewChannelEvent channel)
	{
		final String extension = channel.getExten();
		if (AgentMap.getAgentMap().checkAgentExist(extension))
		{
			final Agent agentToContact = AgentMap.getAgentMap().getAgent(extension);
			if (agentToContact.getUser() == null)
			{
				agentToContact.setUser(new User());
			}
			agentToContact.getUser().setChannelId(channel.getUniqueId());
			agentToContact.getUser().setCallerId(channel.getCallerId());
			LOG.info("Done settng the user info");
		}
		else
		{
			LOG.info("Agent, user trying to call doen't exist");
		}
	}

}
