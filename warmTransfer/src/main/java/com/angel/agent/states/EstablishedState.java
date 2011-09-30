package com.angel.agent.states;

import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ParkAction;
import org.asteriskjava.manager.action.ParkedCallsAction;

public class EstablishedState extends UserState {

    private boolean userParked = false;

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event,
            Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        LOG.info("Received onPropertyChange event " + event.getSource().toString());
        AsteriskChannel channel = (AsteriskChannel) event.getSource();
        if (channel.getState() == ChannelState.HUNGUP) {
            if (userParked == true) {
                processHanupAgent(channel, agent);
            } else {
                super.toInitialState(agent);
            }
        }
    }

    @Override
    public void doParkCall(Agent agent) {
        try {

            LOG.info("Trying to park call " + agent.getUser().getCallerId());
            ParkAction park = new ParkAction(agent.getUser().getChannel().getName(), agent.getChannel().getName());
            ManagerServer.getManagerConnection().sendAction(park);
            Thread.sleep(500);
            ParkedCallsAction parkAction = new ParkedCallsAction();
            ManagerServer.getManagerConnection().sendAction(parkAction);

        } catch (InterruptedException e) {
            LOG.debug("Park action interrupted");
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

    private void processHanupAgent(AsteriskChannel channel, Agent agent) {
        if (channel.getState() == ChannelState.HUNGUP) {
            LOG.info("The agents state is set as hungup");
            agent.setChannel(null);
            agent.setChannelId(null);//It's required to make it null ohterwise the channel id's can be reused by user.
            ParkedCallState parkedCallState = new ParkedCallState();
            agent.setState(parkedCallState);
            //parkedCall.callToAdmin();
        }
    }

    public void processParkedUser() {
        userParked = true;
    }
}
