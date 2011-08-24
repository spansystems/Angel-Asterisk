package com.span.ami;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.NewStateEvent;

public class TalkingToSuperVisorState implements ManagerState {

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event,
			ManagerServer server) {
		if (event.getSource().getClass()
				.equals(server.getUserChannel().getChannel().getClass())) {
			AsteriskChannel channel = (AsteriskChannel) event.getSource();
			System.out
					.println("channel state " + channel.getState().toString());
			// if (event.getSource() == server.getAgentChannel().getChannel()) {

			/*
			 * AsteriskChannel channel = (AsteriskChannel)event.getSource();
			 * if(channel.getState() == ChannelState.UP)
			 * server.getUserChannel().setChannel((AsteriskChannel)channel); }
			 * else if (event.getSource() ==
			 * server.getSuperWiserChannel().getChannel()) {
			 * System.out.println("Supervisor channel channel"); AsteriskChannel
			 * channel = (AsteriskChannel)event.getSource();
			 * server.getSuperWiserChannel().setChannel(channel); if
			 * (channel.getState() == ChannelState.UP) {
			 * server.getAgentChannel().setState(State.ESTABLISHED);
			 * System.out.println
			 * ("Now about to redirect the call to conference");
			 * //bridgeCall(server); //redirectToConference(server); } else if
			 * (channel.getState() == ChannelState.HUNGUP) {
			 * server.getAgentChannel().setState(State.HANGUP);
			 * server.getAgentChannel().setPeerChannel(null); } } else {
			 * System.out.println("Unknown channel"); }
			 */
		} else {
			System.out.println("Unknown property change event " + event);
		}
	}

	/*
	 * private void bridgeCall(ManagerServer server) { try {
	 * System.out.println("Bridgin agent and supervisor together"); BridgeAction
	 * bridge = new
	 * BridgeAction(server.getAgentChannel().getChannel().getName(),
	 * server.getSuperWiserChannel().getChannel().getName(), true);
	 * server.getManagerConnection().sendAction(bridge); //Thread.sleep(10000);
	 * //redirectToConference(server); } catch (IllegalArgumentException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (IllegalStateException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } catch (TimeoutException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

	public void redirectToConference(ManagerServer server) {
		try {
			System.out.println("Redirect the SuperVisor channel to conference");
			server.getSuperVisorChannel().getChannel()
					.redirectBothLegs("meet", "600", 1);

			OriginateAction origin = new OriginateAction();
			origin.setChannel("SIP/200");
			origin.setContext("default");
			origin.setExten("701");
			origin.setPriority(1);
			origin.setTimeout((long) 10000);
			server.getManagerConnection().sendAction(origin);

			// server.getUserChannel().getChannel().redirectBothLegs("meet",
			// "600", 1);

			/*
			 * OriginateAction originateUser = new OriginateAction();
			 * originateUser.setChannel("SIP/200");
			 * originateUser.setContext("default");
			 * originateUser.setExten("600"); originateUser.setPriority(1);
			 * server.getManagerConnection().sendAction(originateUser);
			 */

			/*
			 * server.getAgentChannel().getChannel().redirectBothLegs("meet",
			 * "600", 1); RedirectAction redirect = new
			 * RedirectAction(server.getSuperWiserChannel
			 * ().getChannel().getName(),
			 * server.getSuperWiserChannel().getChannel().getName(), "meet",
			 * "600", 1); server.getManagerConnection().sendAction(redirect);
			 * OriginateAction origin = new OriginateAction();
			 * origin.setChannel(
			 * server.getUserChannel().getChannel().getName());
			 * origin.setContext("pickpark"); origin.setExten("701");
			 * origin.setPriority(1);
			 * server.getManagerConnection().sendAction(origin);
			 */
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
		} catch (ManagerCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("Changing the state to join conference");
			server.changeState(new JoinConferenceState());
		}

	}

	@Override
	public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
		System.out.println("Received NewStateEvent event " + event + " "
				+ getClass().toString());
		// TODO Auto-generated method stub

	}

	@Override
	public void doParkCall(ManagerServer managerServer) {
		// TODO Auto-generated method stub
		System.out.println("Event not handled here");
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
