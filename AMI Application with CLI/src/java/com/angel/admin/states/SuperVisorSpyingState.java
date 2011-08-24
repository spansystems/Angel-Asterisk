package com.angel.admin.states;
import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.NewStateEvent;


public class SuperVisorSpyingState implements UserState{
    String extensionName;
    String extension;
    
    public SuperVisorSpyingState(String name) {
        this.extensionName = name;
        this.extension = extensionName + ",w";
    }

	
    @Override
	public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException,
			IllegalStateException, IOException, TimeoutException {
			System.out.println("Event recieved in SuperVisorSpying State "+event);
                        AsteriskChannel channel = (AsteriskChannel)event.getSource();
                        if(channel.getState() == ChannelState.HUNGUP){
                            admin.setChannel(null);
                            admin.setChannelId(null);
                            admin.setState(new InitialState());
                        }

	}

	@Override
	public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
		// TODO Auto-generated method stub

	}


	@Override
	public void redirectToConference(ManagerServer managerServer) {
		// TODO Auto-generated method stub

	}
	
	public void channelSpy() {		
		System.out.println("Supervisor will spy the channel now");
		try {
			OriginateAction originateUser = new OriginateAction();
			originateUser.setChannel("SIP/300");
			originateUser.setApplication("extenspy");
			originateUser.setData(extension);
			originateUser.setExten("300");
			originateUser.setPriority(1);
			ManagerServer.getManagerConnection().sendAction(originateUser);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
		}
	}

	
	public void setName() {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserState getInstance() {
		// TODO Auto-generated method stub
		return this;
	}

    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doParkCall(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void callToSu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redirectToConference(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
