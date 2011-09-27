package com.angel.agent.states;

import com.angel.agent.Agent;
import com.angel.base.UserState;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;


public class InitialState extends UserState {

    static final Logger logger = Logger.getLogger(InitialState.class);

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
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        logger.info("Received onPropertyChange event " + event.getSource().toString());
        //logger.info("Peerchannelstatus is :"+agent.getPeerChannel());
        // if (event.getSource().getClass().equals(agent.getPeerChannel().getClass())) {
        AsteriskChannel channel = (AsteriskChannel) event.getSource();
        handlePropertyChangeEvent(channel, agent);
        /*} else {
        logger.info("Unknown property change event");
        }*/
    }

    private void processUserChannel(AsteriskChannel channel, Agent agent) {
        agent.getUser().setChannel(channel);
    }

    private void processAgentChannel(AsteriskChannel channel, Agent agent) {
        //TOdo : Handle all the state change events        
        logger.info("Received agent on property change event");
        agent.setChannel(channel);
        if (channel.getState() == ChannelState.UP) {
            logger.info("Status of the system is established");
            logger.info("The agent is now talking to user " + agent.getUser().getCallerId());
            agent.setState(new EstablishedState());
            //agent.getState().doParkCall(agent);
        } else if (channel.getState() == ChannelState.HUNGUP) {
            agent.setChannel(null);

        }
    }

    private void handlePropertyChangeEvent(AsteriskChannel channel, Agent agent) {
        String channelName = agent.getChannelOwner(channel);
        if (channelName.contains("peerChannel")) {
            logger.info("User channel");
            processUserChannel(channel, agent);
        } else if (channelName.contains("agentChannel")) {
            logger.info("Setting Agent channel");
            processAgentChannel(channel, agent);
        } else {
            logger.info("Unknown channel");
        }
    }
}
