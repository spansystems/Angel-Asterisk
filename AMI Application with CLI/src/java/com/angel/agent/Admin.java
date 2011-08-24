/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent;

import com.angel.admin.states.InitialState;
import com.angel.base.AgentInterface;
import com.angel.base.UserState;
import com.angel.manager.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;

/**
 *
 * @author prashanth_p
 */
public final class Admin extends AgentInterface implements PropertyChangeListener {

    private static HashMap<String, Agent> agentMap = new HashMap<String, Agent>();
    private Agent agent = null;
    private String channelId ;

    /**
     * 
     * @return
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * 
     * @param channelId
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * 
     * @return
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * 
     * @param agent
     */
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    /**
     * 
     */
    public Admin() {
        this.state = new InitialState();
    }

    /**
     * 
     * @param agentName
     * @return
     */
    public static boolean checkAgentExist(String agentName) {
        if (agentMap.containsKey(agentName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @param agentName
     * @return
     */
    public static Agent getAgent(String agentName) {
        return agentMap.get(agentName);
    }

    /**
     * 
     * @param agentName
     * @param agent
     */
    public static void setAgent(String agentName, Agent agent) {
        agentMap.put(agentName, agent);
    }

    /**
     * 
     * @return
     */
    @Override
    public AsteriskChannel getChannel() {
        return channel;
    }

    /**
     * 
     * @param channel
     */
    @Override
    public void setChannel(AsteriskChannel channel) {
        this.channel = channel;
    }

    /**
     * 
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    @Override
    public UserState getState() {
        return state;
    }

    /**
     * 
     * @param state
     */
    @Override
    public void setState(UserState state) {
        this.state = state;
    }

    /**
     * 
     * @param event
     * @param server
     */
    public void onNewStateEvent(NewStateEvent event, ManagerServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return
     */
    public Admin getInstance() {
        return this;
    }

    /**
     * 
     * @param channel
     */
    @Override
    public void onNewAsteriskChannel(AsteriskChannel channel) {        
            if (getState().toString().contains("InitialState")) {
                setChannel(channel);
                channel.addPropertyChangeListener(this);
                System.out.print("Received the admin channel " + channel);
            } else {
                System.out.println("UnKnown Asterisk channel for supervisor");
            }         
    }

    public void propertyChange(PropertyChangeEvent evt) {
        try {
            this.state.onPropertyChangeEvent(evt, this);

        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @return
     */
    @Override
    public String getPeerName() {
        return peerName;
    }

    /**
     * 
     * @param peerName
     */
    @Override
    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }
    /**
     * 
     * @return
     */
    @Override
    public AsteriskChannel getPeerChannel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   /**
    * 
    * @param peerchannel
    */
   @Override
    public void setPeerChannel(AsteriskChannel peerchannel) {
       System.out.println("Unimplemented yet");
    }

    /**
     * 
     * @param channel
     */
    public void processEvents(String channel) {
        System.out.println("Receiving admin events for admin:" +channel);
    }
}
