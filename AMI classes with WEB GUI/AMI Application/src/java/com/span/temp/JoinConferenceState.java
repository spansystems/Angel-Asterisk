package com.span.temp;

import com.span.asterisk.ami.FinalState;
import com.span.asterisk.ami.ManagerChannel;
import com.span.asterisk.ami.ManagerServer;
import com.span.asterisk.ami.ManagerState;
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

public class JoinConferenceState implements ManagerState {

    boolean userJoined = false;
    
    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event,
            ManagerServer server) {
        System.out.println("Received onPropertyChange event in joinconference"
                + getClass().toString());
        System.out.println("Event class received is for "
                + event.getSource().getClass().toString());
        
        if (event.getSource().getClass().equals(server.getAgentChannel().getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            System.out.println("channel state for agent "
                    + channel.getState().toString());
            if (channel.getName().contains(server.getUserName())) {
                System.out.println("Received user channel event "
                        + channel.getState().toString());
                ManagerChannel userChannel = server.getUserChannel();
                userChannel.setChannel(channel);
                server.setUserChannel(userChannel);
            } else if (channel.getName().contains(server.getAgentName())) {
                System.out.println("Received agent channel event in join conference class"
                        + channel.getState().toString());
                ManagerChannel agentChannel = server.getAgentChannel();
                agentChannel.setChannel(channel);
                server.setAgentChannel(agentChannel);
                if (channel.getState() == ChannelState.UP) {
                    if (!userJoined) {
                        server.redirectToConference(server);                        
                    }
                } else if (channel.getState() == ChannelState.DOWN
                        || channel.getState() == ChannelState.HUNGUP) {
                    //try {
                        System.out.println("Pulling the user in Conference ");                    
                        //Thread.sleep(500);                        
                        //putAgentToConf(server);
                    /*} catch (InterruptedException ex) {
                        Logger.getLogger(JoinConferenceState.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                }
                
            } else {
                System.out.println("Unknown channel " + channel);
            }
        }
        // pullUserToConference(server);
    }
    
    @Override
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        System.out.println("Received NewStateEvent event " + event + " "
                + getClass().toString());
        // TODO Auto-generated method stub

    }
    
    @Override
    public void doParkCall(ManagerServer managerServer) {
        System.out.println("Event not handled here");
    }
    
    @Override
    public void redirectToConference(ManagerServer server) {
        System.out.println("Redirecting the parked channel to conference");
        try {
            server.getUserChannel().getChannel().redirectBothLegs("meet", "600", 1);            
            
        } catch (ManagerCommunicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchChannelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
        userJoined = true;        
        }
    }
    
    private void putAgentToConf(ManagerServer server) {
        
        try {
            System.out.println("Putting the agent to confernece");
                        
            OriginateAction originateUser = new OriginateAction();
            originateUser.setChannel("SIP/200");
            originateUser.setApplication("meetme");
            originateUser.setData("600,M");
            originateUser.setExten("600");
            originateUser.setPriority(1);
            
            server.getManagerConnection().sendAction(originateUser);    
            server.setCurrentState(new FinalState());
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
}
