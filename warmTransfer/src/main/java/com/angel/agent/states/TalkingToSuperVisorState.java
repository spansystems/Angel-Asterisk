package com.angel.agent.states;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;

public class TalkingToSuperVisorState extends UserState {

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

        AsteriskChannel channel = (AsteriskChannel) event.getSource();
        LOG.info("Asterisk channel in Talking to su state " + channel);
        if (channel.getCallerId().toString().contains(agent.getName()) && channel.getState() == ChannelState.HUNGUP) {
            processAgentChannel(agent, channel);
        }

    }

    /**
     * 
     * @param agent
     */
    @Override
    public void redirectToConference(Agent agent) {
        try {
            LOG.info("Redirect the SuperVisor channel to conference");
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

    private void pickUser(Agent agent) {
        try {
            OriginateAction origin = new OriginateAction();
            LOG.info(agent.getUser().getParkingLotNo());
            String channel = "SIP/" + agent.getName() + "@out";
            origin.setChannel(channel);
            origin.setContext("pickuser");
            origin.setExten(agent.getUser().getParkingLotNo());
            origin.setPriority(1);
            origin.setCallerId(agent.getName());
            origin.setVariable("name", agent.getName());
            origin.setTimeout((long) 10000);
            ManagerServer.getManagerConnection().sendAction(origin);
        } catch (IOException ex) {
            LOG.error("IOException in pick User", ex);
        } catch (TimeoutException ex) {
            LOG.error("Time out Exception in pick User", ex);
        } catch (IllegalArgumentException ex) {
            LOG.error("Illegal argument Exception in pick User", ex);
        } catch (IllegalStateException ex) {
            LOG.error("Illegal state Exception in pick User", ex);
        } finally {
            agent.setState(new JoinConferenceState());
            LOG.info("Changing the state to join conference state");
        }
    }

    private void processAgentChannel(Agent agent, AsteriskChannel channel) {
        if (channel.getState() == ChannelState.HUNGUP) {
            LOG.info("The agent's channel is hungup after putting admin's channel in confernence");
            agent.setChannel(null);
            agent.setChannelId(null);//Required to make it null
            agent.setState(new JoinConferenceState());
            pickUser(agent);
        }
    }

    @Override
    public void hangupOtherEnd(Agent agent) {
        try {
            Admin admin = agent.getAdmin();
            HangupAction hangup = new HangupAction();
            hangup.setChannel(admin.getChannel().toString());
            ManagerServer.getManagerConnection().sendAction(hangup);
        } catch (IOException ex) {
            LOG.error("IOException in hangupOther end User", ex);
        } catch (TimeoutException ex) {
            LOG.error("Time out Exception in hangupOther end User", ex);
        } catch (IllegalArgumentException ex) {
            LOG.error("Illegal argument Exception in hangupOther end User", ex);
        } catch (IllegalStateException ex) {
            LOG.error("Illegal state Exception in hangupOther end User", ex);
        } finally {
            agent.setState(new UnParkUser());
        }
    }
}
