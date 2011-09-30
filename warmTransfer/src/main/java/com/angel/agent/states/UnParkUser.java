/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent.states;

import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;

/**
 * Agent will come to this state when he ends the call with agent2/admin 
 * and wants to pick the parked User call.
 * 
 */
public class UnParkUser extends UserState {

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        if (event.getSource().getClass().equals(agent.getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            if (channel.getName().contains(agent.name) && channel.getState() == ChannelState.HUNGUP) {
                pickUser(agent);
            }
        }
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
            LOG.error("IOException in pick user", ex);
        } catch (TimeoutException ex) {
            LOG.error("Time out Exception in pick user", ex);
        } catch (IllegalArgumentException ex) {
            LOG.error("Illegal argument Exception in pick user", ex);
        } catch (IllegalStateException ex) {
            LOG.error("Illegal state Exception in pick user", ex);
        } finally {
            LOG.info("Changing the state to join conference");
            agent.setState(new JoinConferenceState());
        }
    }
}
