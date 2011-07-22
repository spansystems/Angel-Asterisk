package com.span.ami;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.NewStateEvent;


public class SuperVisorSpyingState implements ManagerState{

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event,
			ManagerServer server) throws IllegalArgumentException,
			IllegalStateException, IOException, TimeoutException {
			System.out.println("Event recieved in SuperVisorSpying State "+event);

	}

	@Override
	public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doParkCall(ManagerServer managerServer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void redirectToConference(ManagerServer managerServer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelSpy(ManagerServer server) {		
		System.out.println("Supervisor will spy the channel now");
		try {
			OriginateAction originateUser = new OriginateAction();
			originateUser.setChannel("SIP/300");
			originateUser.setApplication("extenspy");
			originateUser.setData("200,w");
			originateUser.setExten("300");
			originateUser.setPriority(1);
			server.getManagerConnection().sendAction(originateUser);
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
