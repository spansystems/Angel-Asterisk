package com.angel.agent.states;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
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

/**
 * 
 * @author prashanth_p
 */
public class TalkingToSuperVisorState implements UserState {

    /**
     * 
     * @param event
     * @param agent
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     * @throws TimeoutException
     */
    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event,
            Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        if (event.getSource().getClass().equals(agent.getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            System.out.println("channel state " + channel.getState().toString());
            if (channel.getName().contains(agent.getName()) && channel.getState() == ChannelState.HUNGUP) {
                processAgentChannel(agent, channel);
            }
        } else {
            System.out.println("Unknown property change event " + event);
        }
    }

    /**
     * 
     * @param agent
     */
    public void redirectToConference(Agent agent) {
        try {
            System.out.println("Redirect the SuperVisor channel to conference");
            agent.getAdmin().getChannel().redirect("meet", "600", 1);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
        } catch (ManagerCommunicationException e) {
            // TODO Auto-generated catch block
        } catch (NoSuchChannelException e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * 
     * @param event
     * @param server
     */
    @Override
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        System.out.println("Received NewStateEvent event " + event + " "
                + getClass().toString());
        // TODO Auto-generated method stub

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

    private void pickUser(Agent agent) {
        try {
            OriginateAction origin = new OriginateAction();
            origin.setChannel("SIP/200");
            origin.setContext("default");
            origin.setExten("701");
            origin.setPriority(1);
            origin.setCallerId("parkinglot");
            origin.setTimeout((long) 10000);
            ManagerServer.getManagerConnection().sendAction(origin);
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
            agent.setState(new JoinConferenceState());
        }
    }

    /**
     * 
     * @param evt
     */
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    /**
     * 
     * @param managerServer
     */
    public void redirectToConference(ManagerServer managerServer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void processAgentChannel(Agent agent, AsteriskChannel channel) {
        if (channel.getState() == ChannelState.HUNGUP) {
            agent.setChannel(null);
            agent.setChannelId(null);//Required to make it null
            agent.setState(new JoinConferenceState());
            pickUser(agent);
        }
    }

    /**
     * 
     */
    public void callToSu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
