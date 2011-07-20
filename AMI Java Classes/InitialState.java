import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;

public class InitialState implements ManagerState {

	@Override
	public void onPropertyChangeEvent(PropertyChangeEvent event,
			ManagerServer server) throws IllegalArgumentException,
			IllegalStateException, IOException, TimeoutException {
		// TODO Auto-generated method stub
		System.out.println("Received onPropertyChange event "
				+ getClass().toString());
		if (event.getSource().getClass()
				.equals(server.getUserChannel().getChannel().getClass())) {
			if (event.getSource() == server.getUserChannel().getChannel()) {
				System.out.println("User channel");
				// AsteriskChannel channel = (AsteriskChannel)event.getSource();
				// server.getUserChannel().setChannel(channel.getName());
			} else if (event.getSource() == server.getAgentChannel()
					.getChannel()) {
				System.out.println("Setting Agent channel");
				AsteriskChannel channel = (AsteriskChannel) event.getSource();
				ManagerChannel agentChannel = server.getAgentChannel();
				agentChannel.setChannel(channel);
				server.setAgentChannel(agentChannel);
				if (channel.getState() == ChannelState.UP) {
					server.getAgentChannel().setState(State.ESTABLISHED);
					System.out.println("Status of the system is established");
					server.changeState(new EstablishedState());
					server.parkCall();
				} else if (channel.getState() == ChannelState.HUNGUP) {
					server.getAgentChannel().setState(State.HANGUP);

				}
			} else {
				System.out.println("Unknown channel");
			}
		} else {
			System.out.println("Unknown property change event");
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

	}

	@Override
	public void redirectToConference(ManagerServer managerServer) {
		// TODO Auto-generated method stub
		System.out.println("This event is not handled here" + this);
	}

}
