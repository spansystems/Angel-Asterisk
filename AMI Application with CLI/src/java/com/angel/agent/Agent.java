/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent;

import com.angel.agent.states.InitialState;
import com.angel.base.AgentInterface;
import com.angel.base.UserState;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author prashanth_p
 */
public final class Agent extends AgentInterface implements PropertyChangeListener {

    private String extension = null;
    private Admin admin;
    private String channelId;
    private String agentEvents;

    /**
     * Used to return the Channel ID
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
    public String getPeerChannelId() {
        return peerChannelId;
    }

    /**
     * 
     * @param peerChannelId
     */
    public void setPeerChannelId(String peerChannelId) {
        this.peerChannelId = peerChannelId;
    }
    private String peerChannelId;

    /**
     * 
     * @return
     */
    public String getExtension() {
        return extension;
    }

    /**
     * 
     * @param extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * 
     * @return
     */
    public Admin getAdmin() {
        return admin;
    }

    /**
     * 
     * @param admin
     */
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    /**
     * 
     */
    public Agent() {
        this.state = new InitialState();
    }

    /**
     * 
     * @return
     */
    @Override
    public AsteriskChannel getPeerChannel() {
        return peerChannel;
    }

    /**
     * 
     * @param peerchannel
     */
    @Override
    public void setPeerChannel(AsteriskChannel peerchannel) {
        this.peerChannel = peerchannel;
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
        return this.state;
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
     * @return
     */
    public Agent getInstance() {
        return this;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        try {
            this.getState().onPropertyChangeEvent(evt, this);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     */
    public void parkCall() {
        this.state.doParkCall(this);
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
     * @param receivedChannel
     * @return
     */
    public String getChannel(AsteriskChannel receivedChannel) {
        System.out.println("The channel id received in agent class is :" + receivedChannel.getId());
        if (receivedChannel.getId().equals(getChannelId())) {
            System.out.println("channel received in agent class is agent channel:");
            return "agentChannel";
        } else if (receivedChannel.getId().equals(getPeerChannelId())) {
            System.out.println("channel received in agent class is User channel:");
            return "peerChannel";
        } else {
            System.out.println("channel received in agent class is unknown:");
            return "unKnown";
        }
    }

    /**
     * 
     * @param channel
     */
    @Override
    public void onNewAsteriskChannel(AsteriskChannel channel) {
        if (channel.getId().equals(getChannelId())) {
            setChannel(channel);
            channel.addPropertyChangeListener(this);
        } else if (channel.getId().equals(getPeerChannelId())) {
            setPeerChannel(channel);
            channel.addPropertyChangeListener(this);
        } else {
            System.out.println("Unknown channel event");
        }
    }

    /**
     * 
     * @param string
     */
    public void processEvents(String string) {
        agentEvents = string;
        System.out.println("In process events");
    }

    /**
     * 
     */
    public void displayEvents() {
        if (this.agentEvents.contains("agent")) {
            System.out.println("Received the events in agent class for agent:" + this.name + "and the event is:" + this.agentEvents.split("\\.")[0]);
        } else {
            System.out.println("Received the events for User in agent class for agent:" + this.name + "and the event is:" + this.agentEvents.split("\\.")[0]);
        }
    }
}
