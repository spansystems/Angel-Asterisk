/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent;

import com.angel.agent.states.InitialState;
import com.angel.base.IAgent;
import com.angel.base.UserState;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.TimeoutException;


public final class Agent extends IAgent implements PropertyChangeListener {

    private String extension = null;
    private Admin admin;
    private String channelId;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

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
    public Agent(String name) {
        this.state = new InitialState();
        this.name = name;
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
   
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            getState().onPropertyChangeEvent(evt, this);
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
     * @param receivedChannel
     * @return
     */
    public String getChannelOwner(AsteriskChannel receivedChannel) {
        //System.out.println("The channel id received in agent class is :" + receivedChannel.getId());
        if (receivedChannel.getId().equals(getChannelId())) {
            System.out.println("channel received in agent class is agent channel:");
            return "agentChannel";
        } else if (receivedChannel.getId().equals(user.getChannelId())) {
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
        } else if (channel.getId().equals(user.getChannelId())) {
            if (!channel.getName().contains("Parked")) {
                user.setChannel(channel);
                System.out.println("User channel set is :" + channel);
            }
            channel.addPropertyChangeListener(this);
        } else {
            System.out.println("Unknown channel event");
        }
    }
}
