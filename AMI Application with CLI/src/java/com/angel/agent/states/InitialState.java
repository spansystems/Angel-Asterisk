package com.angel.agent.states;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;

/**
 * 
 * @author prashanth_p
 */
public class InitialState implements UserState {

    /**
     * 
     * @param event
     * @param agent
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     * @throws TimeoutException
     */
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        System.out.println("Received onPropertyChange event " + getClass().toString());
        System.out.println("Peerchannelstatus is :"+agent.getPeerChannel());
       // if (event.getSource().getClass().equals(agent.getPeerChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            handlePropertyChangeEvent(channel, agent);
        /*} else {
            System.out.println("Unknown property change event");
        }*/
    }

    /**
     * 
     */
    public void setName() {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @return
     */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     * @return
     */
    @Override
    public UserState getInstance() {
        // TODO Auto-generated method stub
        return this;
    }

    /**
     * 
     * @param event
     * @param server
     */
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param agent
     */
    public void parkCall(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param managerServer
     */
    public void redirectToConference(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param managerServer
     */
    public void channelSpy(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param event
     * @param admin
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     * @throws TimeoutException
     */
    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void processUserChannel(AsteriskChannel channel, Agent agent) {       
        agent.setPeerChannel(channel);
    }

    private void processAgentChannel(AsteriskChannel channel, Agent agent) {
        //TOdo : Handle all the state change events        
        agent.setChannel(channel);       
        if (channel.getState() == ChannelState.UP) {
            System.out.println("Status of the system is established");
            System.out.println("The agent is now talking to user " + agent.getPeerChannel().getName());
            agent.setState(new EstablishedState());
            //agent.getState().doParkCall(agent);
        } else if (channel.getState() == ChannelState.HUNGUP) {
            agent.setChannel(null);

        }
    }

    private void handlePropertyChangeEvent(AsteriskChannel channel, Agent agent) {
        String channelName = agent.getChannel(channel);
        if (channelName.contains("peerChannel")) {
            System.out.println("User channel");
            processUserChannel(channel, agent);
        } else if (channelName.contains("agentChannel")) {
            System.out.println("Setting Agent channel");
            processAgentChannel(channel, agent);
        } else {
            System.out.println("Unknown channel");
        }
    }

    /**
     * 
     * @param agent
     */
    public void doParkCall(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     */
    public void callToSu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param agent
     */
    public void redirectToConference(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
