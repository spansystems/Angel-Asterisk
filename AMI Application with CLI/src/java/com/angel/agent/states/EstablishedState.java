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
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ParkAction;
import org.asteriskjava.manager.action.ParkedCallsAction;
import org.asteriskjava.manager.event.NewStateEvent;

public class EstablishedState implements UserState {
    private boolean userParked = false;

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event,
            Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        System.out.println("Received onPropertyChange event " + getClass().toString());
        if (agent.getChannel() == (AsteriskChannel) event.getSource()) {
            System.out.println("Received a property change for agent channel");           
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            if (channel.getState() == ChannelState.HUNGUP) {                
                if(userParked == true)
                    processHanupAgent(channel, agent);                    
                else
                agent.setState(new InitialState());
            }
        }
    }

    @Override
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        System.out.println("Received NewStateEvent event " + event + " "
                + getClass().toString());
        // TODO Auto-generated method stub

    }

    public void doParkCall(Agent agent) {
        try {

            System.out.println("Trying to park call " + agent.getPeerChannel().getName());
            ParkAction park = new ParkAction(agent.getPeerChannel().getName(), agent.getChannel().getName());
            ManagerServer.getManagerConnection().sendAction(park);            
            Thread.sleep(500);
            ParkedCallsAction parkAction = new ParkedCallsAction();
            ManagerServer.getManagerConnection().sendAction(parkAction);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(EstablishedState.class.getName()).log(Level.SEVERE, null, ex);
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

    public void redirectToConference(ManagerServer managerServer) {
        // TODO Auto-generated method stub
        System.out.println("This event is not handled here :" + this);
    }

    public void channelSpy(ManagerServer managerServer) {
        // TODO Auto-generated method stub
    }

    public void setName() {
        // TODO Auto-generated method stub
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public UserState getInstance() {
        // TODO Auto-generated method stub
        return this;
    }

    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void processHanupAgent(AsteriskChannel channel, Agent agent) {
        if (channel.getState() == ChannelState.HUNGUP) {
            System.out.println("The agents state is set as hungup");
            agent.setChannel(null);
            agent.setChannelId(null);//It's required to make it null ohterwise the channel id's can be reused by user.
            ParkedCallState parkedCall = new ParkedCallState();
            agent.setState(parkedCall);
            //parkedCall.callToSu();
        }
    }

    public void callToSu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redirectToConference(Agent agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void processParkedUser() {
        userParked = true;
    }
}
