import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ParkedCallsAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.response.ManagerResponse;

public class ManagerServer implements AsteriskServerListener,
PropertyChangeListener, ManagerEventListener {
	private AsteriskServer asteriskServer;
	ManagerConnection managerConnection;
	private ManagerChannel userChannel;
	private ManagerChannel agentChannel;
	private ManagerChannel superVisorChannel;
	private String userName = "100";
	private String agentName = "200";
	private String suName = "300";
	private ManagerState currentState = new InitialState();

	public ManagerServer() {
		asteriskServer = new DefaultAsteriskServer("10.11.201.71", "admin",
		"span");
		managerConnection = asteriskServer.getManagerConnection();

	}

	public void run() throws ManagerCommunicationException,
	IllegalArgumentException, IllegalStateException, IOException,
	TimeoutException {

		ManagerResponse managerResponse;
		asteriskServer.addAsteriskServerListener(this);
		managerConnection.addEventListener(this);		
		StatusAction action = new StatusAction();
		managerResponse = managerConnection.sendAction(action);
		ParkedCallsAction parkAction = new ParkedCallsAction();
		managerResponse = managerConnection.sendAction(parkAction);
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public void onNewAsteriskChannel(AsteriskChannel channel) {
		System.out.println("Channel : " + channel);
		channel.addPropertyChangeListener(this);
		if (channel.getName().contains(userName)) {
			System.out.println("Received a user channel event");
			if (userChannel == null) {
				userChannel = new ManagerChannel();
				userChannel.setName(channel.getCallerId().getName());
				userChannel.setState(State.EARLY);
				userChannel.setChannel(channel);
				System.out.println("User channel is : " + channel);
			}
		} else if (channel.getName().contains(agentName)) {
			System.out.println("Received an agent channel event");
			agentChannel = new ManagerChannel();
			agentChannel.setName(channel.getCallerId().getName());
			agentChannel.setState(State.EARLY);
			agentChannel.setChannel(channel);
			System.out.println("Agent channel is : " + channel);
			if (channel.getDialingChannel() == userChannel.getChannel()) {
				System.out.print("Settring the dialing channel");
				agentChannel.setPeerChannel(userChannel);
				userChannel.setPeerChannel(agentChannel);
			}
		} else if (channel.getName().contains(suName)) {
			System.out
			.println("Supervisor channel event, setting the channel in the manager agent");
			superVisorChannel = new ManagerChannel();
			superVisorChannel.setName(channel.getCallerId().getName());
			superVisorChannel.setState(State.EARLY);
			superVisorChannel.setChannel(channel);
			
		}
	}


	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		System.out.println(propertyChangeEvent);
		try {
			getCurrentState().onPropertyChangeEvent(propertyChangeEvent, this);
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

	public ManagerConnection getManagerConnection() {
		return managerConnection;
	}

	public void setManagerConnection(ManagerConnection managerConnection) {
		this.managerConnection = managerConnection;
	}

	public ManagerChannel getUserChannel() {
		return userChannel;
	}

	public void setUserChannel(ManagerChannel userChannel) {
		this.userChannel = userChannel;
	}

	public ManagerChannel getAgentChannel() {
		return agentChannel;
	}

	public void setAgentChannel(ManagerChannel agentChannel) {
		this.agentChannel = agentChannel;
	}

	public ManagerChannel getSuperVisorChannel() {
		return superVisorChannel;
	}

	public void setSuperVisorChannel(ManagerChannel superWiserChannel) {
		this.superVisorChannel = superWiserChannel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getSuName() {
		return suName;
	}

	public void setSuName(String suName) {
		this.suName = suName;
	}

	public ManagerState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(ManagerState currentState) {
		this.currentState = currentState;
	}

	public static void main(String[] args) throws Exception {
		ManagerServer helloLiveEverything = new ManagerServer();
		helloLiveEverything.run();
	}


	@Override
	public void onManagerEvent(ManagerEvent event) {
	
		System.out.println("New manager event received: "+event);
	}

	private void onNewStateEvent(NewStateEvent event) { 
	
		System.out.println("New State change event received: "+event);
	}



	public void parkCall() throws IllegalArgumentException,
	IllegalStateException, IOException, TimeoutException {
		getCurrentState().doParkCall(this);
	}

	private void onStatusEvent(StatusEvent statusEvent) {
		// TODO Auto-generated method stub
		System.out.println("onstatusEvent received");

	}

	private void onHangupEvent(ManagerEvent event) {
		System.out.print("onHangup event received");

	}

	private void onDtmfEvent(ManagerEvent event) {
		System.out.println("Received dtmf event");
		System.out.println(event);

	}

	private void onDialEvent(DialEvent dialEvent) {
		System.out.println("Received on dial event");
		System.out.println(dialEvent);

	}

	private void onParkedEvent(ParkedCallEvent parkedEvent) { 
	}


	public void changeState(ManagerState state) {
		setCurrentState(state);
	}

	public void redirectToConference() {
		getCurrentState().redirectToConference(this);

	}

	@Override
	public void onNewAgent(AsteriskAgentImpl arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewMeetMeUser(MeetMeUser arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewQueueEntry(AsteriskQueueEntry arg0) {
		// TODO Auto-generated method stub
		
	}

}
