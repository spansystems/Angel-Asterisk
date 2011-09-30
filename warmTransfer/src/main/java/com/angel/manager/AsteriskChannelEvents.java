/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.manager;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;

/**
 *
 * @author prashanth_p
 */
public class AsteriskChannelEvents extends IManager implements AsteriskServerListener {

    void run() {
        ManagerServer.getAsteriskServer().addAsteriskServerListener(this);
    }

    @Override
    public void onNewAsteriskChannel(AsteriskChannel channel) {
        LOG.info("Asterisk Channel received: " + channel);
        String channelId = channel.getId();
        Agent userAgent = getUserAgentForTheChannel(channelId);
        if (userAgent != null) {
            userAgent.onNewAsteriskChannel(channel);
        } else {
            Admin localAdmin = getAdminChannelID(channelId);
            if (null != localAdmin) {
                localAdmin.onNewAsteriskChannel(channel);
                LOG.info("In Admin new asterisk channel");
            } else {
                LOG.info("Asterisk channel Unidentified:" + channel);
            }
        }
    }

    @Override
    public void onNewMeetMeUser(MeetMeUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNewAgent(AsteriskAgentImpl agent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNewQueueEntry(AsteriskQueueEntry entry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /*
     * Checks and returns the instance Agent(only) for Asterisk Channel event received.
     */

    public Agent getUserAgentForTheChannel(String id) {
        Agent agentTemp = AgentMap.getAgentById(id);
        if (null != agentTemp) {
            LOG.info("Returning the agent for the channel id " + id);
            return agentTemp;
        } else {
            return null;
        }
    }
    /*
     * Returns admin instance according as the Asterisk channel received.
     */

    public Admin getAdminChannelID(String id) {
        Admin adminTemp = AdminMap.getAdminById(id);
        if (null != adminTemp) {
            return adminTemp;
        } else {
            return null;
        }
    }
}
