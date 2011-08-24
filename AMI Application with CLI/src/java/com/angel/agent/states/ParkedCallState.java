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

public class ParkedCallState implements UserState {

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent)throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        System.out.println("Received onPropertyChange event " + getClass().toString());
        if (event.getSource().getClass().equals(agent.getPeerChannel().getClass())) {
            System.out.println("Asterisk channel state change event received" + event);
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            System.out.println("Dialled channel is: "+channel.getDialedChannel());
            if (channel.getDialedChannel() != null && channel.getName().contains("200")) {
                processAdmin(channel, agent);
            }
            if (channel.getState() == ChannelState.UP) {
                System.out.println("Agent state is up:" + channel.getState());
            }
        }
    }

    @Override
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        // TODO Auto-generated method stub
    }

    @Override
    public void redirectToConference(ManagerServer managerServer) {
        System.out.println("This event is not handled here: " + this);

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

    public void callToSu() {
        try {
            OriginateAction origin = new OriginateAction();
            origin.setChannel("SIP/200");
            origin.setContext("default");
            origin.setExten("300");
            origin.setVariable("extension", "300");
            origin.setCallerId("agent");
            origin.setPriority(1);
            origin.setTimeout((long) 10000);
            ManagerServer.getManagerConnection().sendAction(origin);
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

    public void doParkCall(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void processAdminChannel(AsteriskChannel channel, Agent agent) {
        System.out.println("Received a property change for Supervisor channel" + channel);
        TalkingToSuperVisorState ta = new TalkingToSuperVisorState();
        agent.setState(ta);
        //ta.redirectToConference(agent);
    }

    private void processAdmin(AsteriskChannel channel, Agent agent) {
        AsteriskChannel dialedChannel = channel.getDialedChannel();
        String id = dialedChannel.getId();
        Admin admin = ManagerServer.getAdmin("300");
        if (admin.getChannelId().equals(id)) {            
            admin.setAgent(agent);
            agent.setAdmin(admin);
        } else {
            System.out.println("Admin is not present or busy");
        }
    }

    public void redirectToConference(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
