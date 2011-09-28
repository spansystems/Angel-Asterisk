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

public class JoinConferenceState extends UserState {

    boolean userJoined = false;

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException,
            IllegalStateException, TimeoutException {
        logger.info("Event class received in JoinConference for " + event.getSource().getClass().toString());
        if (event.getSource().getClass().equals(agent.getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            String channelName = agent.getChannelOwner(channel);
            logger.info("channel Name received is" + channelName);
            logger.info("channel state for agent" + channel.getState().toString());
            if (channelName.equals("peerChannel")) {
                processPeerChannel(agent, channel);
            } else if (channelName.equals("agentChannel")) {
                processAgentChannel(channel, agent);
            } else {
                logger.info("Unknown channel " + channel);
            }
        }
    }

    @Override
    public void redirectToConference(Agent agent) {
        logger.info("Redirecting the parked channel to conference");
        agent.getUser().getChannel().redirect("meet", "600", 1);
        userJoined = true;
    }

    private void pullAgentToConference(Agent agent) {
        try {
            logger.info("Putting the agent to confernece");
            OriginateAction originateUser = new OriginateAction();
            originateUser.setChannel("SIP/200");
            originateUser.setApplication("meetme");
            originateUser.setData("600,M");
            originateUser.setExten("200");
            originateUser.setPriority(1);
            ManagerServer.getManagerConnection().sendAction(originateUser);
            agent.setState(new FinalState());
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

    private void processPeerChannel(Agent agent, AsteriskChannel channel) {
        logger.info("Received user channel event "
                + channel.getState().toString());
        agent.getUser().setChannel(channel);
    }

    private void processAgentChannel(AsteriskChannel channel, Agent agent) {
        logger.info("Received agent channel event " + channel.getState().toString());
        agent.setChannel(channel);
        if (channel.getState() == ChannelState.UP) {
            if (!userJoined) {
                redirectToConference(agent);
            }
        } else if (channel.getState() == ChannelState.HUNGUP) {
            logger.info("Pulling the user in Conference ");
            if (userJoined == true) {
                pullAgentToConference(agent);
            }
        }
    }
}
