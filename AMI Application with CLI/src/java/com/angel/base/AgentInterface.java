/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.base;

import org.asteriskjava.live.AsteriskChannel;

/**
 *
 * @author prashanth_p
 */
public abstract class AgentInterface {

    public String name;
    public String peerName;
    public AsteriskChannel peerChannel;
    public AsteriskChannel channel;
    public UserState state;
    public abstract String getPeerName();

    public abstract void setPeerName(String peerName);
    
    public abstract void setState(UserState state);

    public abstract AsteriskChannel getPeerChannel();

    public abstract void setPeerChannel(AsteriskChannel peerchannel);

    public abstract UserState getState();    

    public abstract AsteriskChannel getChannel();

    public abstract void setChannel(AsteriskChannel channel);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract void onNewAsteriskChannel(AsteriskChannel channel);
}
