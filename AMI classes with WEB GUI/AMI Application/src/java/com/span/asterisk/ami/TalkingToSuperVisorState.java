package com.span.asterisk.ami;

import com.span.temp.JoinConferenceState;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.NewStateEvent;

public class TalkingToSuperVisorState implements ManagerState {

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event,
            ManagerServer server) {
        if (event.getSource().getClass().equals(server.getUserChannel().getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            System.out.println("channel state " + channel.getState().toString());
            if (channel.getName().contains(server.getAgentName())) {
                if (channel.getState() == ChannelState.DOWN
                        || channel.getState() == ChannelState.HUNGUP) {
                    pickUser(server);
                }
            }

        } else {
            System.out.println("Unknown property change event " + event);
        }
    }

    public void redirectToConference(ManagerServer server) {
        try {
            System.out.println("Redirect the SuperVisor channel to conference");
            server.getSuperVisorChannel().getChannel().redirectBothLegs("meet", "600", 1);            

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ManagerCommunicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchChannelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    private void pickUser(ManagerServer server) {
        try {
            OriginateAction origin = new OriginateAction();
            origin.setChannel("SIP/200");
            origin.setContext("default");
            origin.setExten("701");
            origin.setPriority(1);
            origin.setCallerId("parkinglot");
            origin.setTimeout((long) 10000);
            server.getManagerConnection().sendAction(origin);
        } catch (IOException ex) {
            Logger.getLogger(TalkingToSuperVisorState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(TalkingToSuperVisorState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TalkingToSuperVisorState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(TalkingToSuperVisorState.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Changing the state to join conference");
            server.changeState(new JoinConferenceState());
        }
    }
}
