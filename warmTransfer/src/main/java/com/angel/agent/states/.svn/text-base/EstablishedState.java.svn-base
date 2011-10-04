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

/**
 * User state handler.
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class EstablishedState extends UserState {

    private boolean userParked;

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event,
            Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        LOG.info("Received onPropertyChange event " + event.getSource().toString());
        final AsteriskChannel channel = (AsteriskChannel) event.getSource();
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
            final ParkAction park = new ParkAction(agent.getUser().getChannel().getName(), agent.getChannel().getName());
            ManagerServer.getManagerConnection().sendAction(park);
            Thread.sleep(500);
            final ParkedCallsAction parkAction = new ParkedCallsAction();
            ManagerServer.getManagerConnection().sendAction(parkAction);

        } catch (InterruptedException e) {
            LOG.warn("Park action interrupted");
        } catch (IllegalArgumentException e) {
            LOG.warn("Illegal argument exception");
        } catch (IllegalStateException e) {
            LOG.warn("illegal state exception");
        } catch (IOException e) {
            LOG.warn("IO exception while sending action msg");
        } catch (TimeoutException e) {
            LOG.warn("Timeout exception while sending action");
        }
    }

    private void processHanupAgent(AsteriskChannel channel, Agent agent) {
        if (channel.getState() == ChannelState.HUNGUP) {
            LOG.info("The agents state is set as hungup");
            agent.setChannel(null);
            agent.setChannelId(null); //It's required to make it null ohterwise the channel id's can be reused by user.
            final ParkedCallState parkedCallState = new ParkedCallState();
            agent.setState(parkedCallState);
            //parkedCall.callToAdmin();
        }
    }

    public void processParkedUser() {
        userParked = true;
    }
}
