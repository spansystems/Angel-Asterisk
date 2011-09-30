package com.angel.agent.states;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.AdminMap;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.TimeoutException;

public class ParkedCallState extends UserState {

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Agent agent) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        //if (event.getSource().getClass().equals(agent.getChannel().getClass())) {
        LOG.info("Property change event received in ParkedCall State" + event);
        AsteriskChannel channel = (AsteriskChannel) event.getSource();
        LOG.info("Dialled channel is: " + channel.getDialedChannel());
        if (channel.getDialedChannel() != null) {
            processAdmin(channel, agent);
        }
        if (channel.getState() == ChannelState.UP) {
            LOG.info("Agent state is up:" + channel.getState());
        }
    }
    /*
     * 
     * This method is called from admin's established state after the channel for admin is up.
     */

    public void processAdminChannel(AsteriskChannel channel, Agent agent) {
        LOG.info("Received a property change for Supervisor channel" + channel);
        TalkingToSuperVisorState ta = new TalkingToSuperVisorState();
        agent.setState(ta);
    }

    private void processAdmin(AsteriskChannel channel, Agent agent) {
        AsteriskChannel dialedChannel = channel.getDialedChannel();
        String id = dialedChannel.getId();
        Admin admin = AdminMap.getAdminById(id);
        if (admin.getChannelId().equals(id)) {
            admin.setAgent(agent);
            agent.setAdmin(admin);
        } else {
            LOG.info("Admin is not present or busy");
        }
    }
}
