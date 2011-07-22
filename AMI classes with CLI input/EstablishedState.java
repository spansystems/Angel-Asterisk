package com.span.ami;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ParkAction;
import org.asteriskjava.manager.event.NewStateEvent;

public class EstablishedState implements ManagerState {
	// boolean flag = false;
	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event,
			ManagerServer server) {
		// TODO Auto-generated method stub
		System.out.println("Received onPropertyChange event "
				+ getClass().toString());
		if (event.getSource() == server.getAgentChannel().getChannel()) {
			if (server.getAgentChannel().getChannel() == (AsteriskChannel) event
					.getSource()) {
				System.out
				.println("Received a property change for agent channel");
				AsteriskChannel channel = (AsteriskChannel) event.getSource();
				if (channel.getState() == ChannelState.HUNGUP) {
					// flag = true;
					//server.changeState(new ParkedCallState());
					// BridgeCall(server);
					server.getAgentChannel().setState(State.HANGUP);
					server.getAgentChannel().setPeerChannel(null);
				}
			}
		}

	}

	/*
	 * private void BridgeCall(ManagerServer server) { try {
	 * System.out.println("Trying to bridge the call between agent and supervisor."
	 * ); OriginateAction originateToUser = new OriginateAction();
	 * //originateToUser.setChannel("SIP/200"); originateToUser.setExten("200");
	 * originateToUser.setContext("default"); originateToUser.setPriority(new
	 * Integer(1)); originateToUser.setTimeout(new Long(30000));
	 * server.getManagerConnection().sendAction(originateToUser);
	 * 
	 * OriginateAction originateToAgent = new OriginateAction();
	 * //originateToAgent.setChannel("SIP/300");
	 * originateToAgent.setExten("300"); originateToAgent.setContext("default");
	 * originateToAgent.setPriority(new Integer(1));
	 * originateToAgent.setTimeout(new Long(30000));
	 * 
	 * server.getManagerConnection().sendAction(originateToAgent);
	 * 
	 * 
	 * 
	 * } catch (IllegalArgumentException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IllegalStateException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (IOException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (TimeoutException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	@Override
	public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
		System.out.println("Received NewStateEvent event " + event + " "
				+ getClass().toString());
		// TODO Auto-generated method stub

	}

	public void doParkCall(ManagerServer managerServer) {
		try {
			System.out.println("Trying to park call "
					+ managerServer.getUserChannel().getChannel().getName());

			ParkAction park = new ParkAction(managerServer.getUserChannel()
					.getChannel().getName(), managerServer.getAgentChannel()
					.getChannel().getName());
			// park.setTimeout(new Integer(300));
			managerServer.getManagerConnection().sendAction(park);

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

	@Override
	public void redirectToConference(ManagerServer managerServer) {
		// TODO Auto-generated method stub
		System.out.println("This event is not handled here :" + this);
	}

	@Override
	public void channelSpy(ManagerServer managerServer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagerState getInstance() {
		// TODO Auto-generated method stub
		return this;
	}

}
