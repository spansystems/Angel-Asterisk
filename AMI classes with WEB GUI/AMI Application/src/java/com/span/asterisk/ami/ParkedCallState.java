package com.span.asterisk.ami;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.NewStateEvent;

public class ParkedCallState implements ManagerState {

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, ManagerServer server) {
        System.out.println("Received onPropertyChange event " + getClass().toString());
        if (event.getSource().getClass().equals(server.getUserChannel().getChannel().getClass())) {
            System.out.println("Asterisk channel state change event received");
            if (server.getSuperVisorChannel() != null) {
                System.out.println("state change received for supervisor");
                if (server.getSuperVisorChannel().getChannel() == (AsteriskChannel) event.getSource()) {
                    System.out.println("Received a property change for Supervisor channel");
                    AsteriskChannel channel = (AsteriskChannel) event.getSource();
                    if (channel.getState() == ChannelState.UP) {
                        server.getAgentChannel().setState(State.ESTABLISHED);
                        server.getSuperVisorChannel().setState(State.ESTABLISHED);
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
        System.out.println("This event is not handled here: " + this);

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

    public void callToSu(ManagerServer server) {

        try {
            OriginateAction origin = new OriginateAction();
            origin.setChannel("SIP/300");
            origin.setContext("default");
            origin.setExten("200");
            origin.setCallerId("agent");
            origin.setPriority(1);
            origin.setTimeout((long) 10000);
            server.getManagerConnection().sendAction(origin);
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
}
