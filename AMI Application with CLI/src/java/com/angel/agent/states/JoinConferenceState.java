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
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.NewStateEvent;

public class JoinConferenceState implements UserState {

    boolean userJoined = false;

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent)throws IllegalArgumentException,
            IllegalStateException, TimeoutException {
        System.out.println("Event class received in JoinConference for " + event.getSource().getClass().toString());
        if (event.getSource().getClass().equals(agent.getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            String channelName = agent.getChannel(channel);
            System.out.println("channel Name received is" + channelName);
            System.out.println("channel state for agent" + channel.getState().toString());
            if (channelName.equals("peerChannel")) {
                processPeerChannel(agent, channel);
            } else if (channelName.equals("agentChannel")) {
                processAgentChannel(channel, agent);
            } else {
                System.out.println("Unknown channel " + channel);
            }
        }        
    }

    @Override
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        System.out.println("Received NewStateEvent event " + event + " "
                + getClass().toString());
        // TODO Auto-generated method stub

    }

    public void redirectToConference(Agent agent) {
        System.out.println("Redirecting the parked channel to conference");
        agent.getPeerChannel().redirect("meet", "600", 1);
        userJoined = true;
    }

    private void pullAgentToConference(Agent agent) {
        try {
            System.out.println("Putting the agent to confernece");
            OriginateAction originateUser = new OriginateAction();
            originateUser.setChannel("SIP/200");
            originateUser.setApplication("meetme");
            originateUser.setData("600,M");
            originateUser.setExten("200");
            originateUser.setPriority(1);
            ManagerServer.getManagerConnection().sendAction(originateUser);
            agent.setState(new FinalState());
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

    public void channelSpy(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UserState getInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doParkCall(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redirectToConference(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void processPeerChannel(Agent agent, AsteriskChannel channel) {
        System.out.println("Received user channel event "
                + channel.getState().toString());
        agent.setPeerChannel(channel);
    }

    private void processAgentChannel(AsteriskChannel channel, Agent agent) {
        System.out.println("Received agent channel event "+ channel.getState().toString());        
        agent.setChannel(channel);        
        if (channel.getState() == ChannelState.UP) {
            if (!userJoined) {
                redirectToConference(agent);
            }
        } else if (channel.getState() == ChannelState.HUNGUP) {
            System.out.println("Pulling the user in Conference ");
            if (userJoined == true) {
                pullAgentToConference(agent);
            }
        }
    }

    public void callToSu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
