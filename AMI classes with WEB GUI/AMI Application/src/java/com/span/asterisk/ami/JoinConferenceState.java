package com.span.asterisk.ami;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.NewStateEvent;

public class JoinConferenceState implements ManagerState {
	boolean userJoined = false;

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event,
			ManagerServer server) {
		System.out.println("Received onPropertyChange event in joinconference"
				+ getClass().toString());
		System.out.println("Event class received is for "
				+ event.getSource().getClass().toString());

		if (event.getSource().getClass()
				.equals(server.getSuperVisorChannel().getChannel().getClass())) {
			AsteriskChannel channel = (AsteriskChannel) event.getSource();
			System.out.println("channel state for agent"
					+ channel.getState().toString());
			if (channel.getName().contains(server.getUserName())) {
				System.out.println("Received user channel event "
						+ channel.getState().toString());
				ManagerChannel userChannel = server.getUserChannel();
				userChannel.setChannel(channel);
				server.setUserChannel(userChannel);
			} else if (channel.getName().contains(server.getAgentName())) {
				System.out.println("Received agent channel event "
						+ channel.getState().toString());
				ManagerChannel agentChannel = server.getAgentChannel();
				agentChannel.setChannel(channel);
				server.setAgentChannel(agentChannel);
				if (channel.getState() == ChannelState.UP) {
					if (!userJoined) {
						server.redirectToConference(server);
					}
				} else if (channel.getState() == ChannelState.DOWN
						|| channel.getState() == ChannelState.HUNGUP) {
					System.out.println("Pulling the user in Conference ");
					if (userJoined == true) {
						pullAgentToConference(server);
					}

				}

			} else {
				System.out.println("Unknown channel " + channel);
			}
		}
		// pullUserToConference(server);
	}

	@Override
	public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
		System.out.println("Received NewStateEvent event " + event + " "
				+ getClass().toString());
		// TODO Auto-generated method stub

	}

	@Override
	public void doParkCall(ManagerServer managerServer) {
		System.out.println("Event not handled here");
	}

	@Override
	public void redirectToConference(ManagerServer server) {
		System.out.println("Redirecting the parked channel to conference");
		server.getUserChannel().getChannel().redirect("meet", "600", 1);
		userJoined = true;
                //
	}

	private void pullAgentToConference(ManagerServer server) {

		try {
			System.out.println("Putting the agent to confernece");
			OriginateAction originateUser = new OriginateAction();
			originateUser.setChannel("SIP/200");
			originateUser.setApplication("meetme");
			originateUser.setData("600,M");
			originateUser.setExten("200");
			originateUser.setPriority(1);
			server.getManagerConnection().sendAction(originateUser);
			server.changeState(new FinalState());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void channelSpy(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ManagerState getInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
