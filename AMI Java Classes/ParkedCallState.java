import java.beans.PropertyChangeEvent;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.event.NewStateEvent;


public class ParkedCallState implements ManagerState{

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event, ManagerServer server) {
		System.out.println("Received onPropertyChange event " +getClass().toString());
		if (event.getSource().getClass().equals(server.getUserChannel().getChannel().getClass())) {
			System.out.println("Asterisk channel state change event received");
			if (server.getSuperVisorChannel() != null) {
				System.out.println("state change received for supervisor");
				if (server.getSuperVisorChannel().getChannel() == (AsteriskChannel) event.getSource()) {
					System.out.println("Received a property change for Supervisor channel");
					AsteriskChannel channel = (AsteriskChannel) event.getSource();
					if (channel.getState() == ChannelState.UP) {					 
						server.changeState(new TalkingToSuperVisorState());
						server.redirectToConference();
						//BridgeCall(server);
					}
				}
			}
		}
	}

	@Override
	public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doParkCall(ManagerServer managerServer) {
		System.out.println("Event not handled here");		
	}

	@Override
	public void redirectToConference(ManagerServer managerServer) {
		System.out.println("This event is not handled here: "+ this);
		
	}

}
