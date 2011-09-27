/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent;

import com.angel.admin.states.InitialState;
import com.angel.base.IAgent;
import com.angel.base.UserState;
import com.angel.manager.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;


public final class Admin extends IAgent implements PropertyChangeListener {

    private Agent agent = null;
    private String channelId;

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
    public Admin(String name) {
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
        setChannel(channel);
        channel.addPropertyChangeListener(this);
        System.out.print("Received the admin channel " + channel);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        try {
            state.onPropertyChangeEvent(evt, this);
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
}
