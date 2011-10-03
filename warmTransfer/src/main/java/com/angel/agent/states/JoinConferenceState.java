package com.angel.agent.states;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.RedirectAction;

import com.angel.agent.Agent;
import com.angel.base.ChannelOwner;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;

public class JoinConferenceState extends UserState {

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException, IllegalStateException,
          TimeoutException {
        LOG.info("Event class received in JoinConference for " + event.getSource().getClass().toString());
        if (event.getSource().getClass().equals(agent.getChannel().getClass())) {
            AsteriskChannel channel = (AsteriskChannel) event.getSource();
            ChannelOwner channelName = agent.getChannelOwner(channel);
            LOG.info("channel Name received is" + channelName);
            LOG.info("channel state for agent" + channel.getState().toString());
            if (channelName.equals(ChannelOwner.USER)) {
                processPeerChannel(agent, channel);
            } else if (channelName.equals(ChannelOwner.AGENT)) {
                processAgentChannel(channel, agent);
            } else {
                LOG.info("Unknown channel " + channel);
            }
        }
    }

    @Override
    public void redirectToConference(Agent agent) {
        LOG.info("Redirecting the parked channel to conference");
        RedirectAction action = new RedirectAction();
        action.setChannel(agent.getUser().getChannelAfterUnpark());
        action.setExtraChannel(agent.getChannel().getName());
        action.setExten("600");
        action.setPriority(1);
        action.setContext("meet");
        action.setExtraContext("meet");
        action.setExtraExten("600");
        action.setExtraPriority(1);

        try {
            ManagerServer.getManagerConnection().sendAction(action);
            agent.setState(new FinalState());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    private void processPeerChannel(Agent agent, AsteriskChannel channel) {
        LOG.info("Received user channel event " + channel.getState().toString());
        agent.getUser().setChannel(channel);
    }

    private void processAgentChannel(AsteriskChannel channel, Agent agent) {
        LOG.info("Received agent channel event " + channel.getState().toString());
        agent.setChannel(channel);
        if (channel.getState() == ChannelState.UP) {
            redirectToConference(agent);
        } else if (channel.getState() == ChannelState.HUNGUP) {
            LOG.error("Agent channel hungup while putting him in the conference ");
        }
    }
}
