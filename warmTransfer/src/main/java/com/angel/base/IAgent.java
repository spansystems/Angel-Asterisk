/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.base;

import org.asteriskjava.live.AsteriskChannel;

public abstract class IAgent {

    public String name;
    public String peerName;
    public AsteriskChannel peerChannel;
    public AsteriskChannel channel;
    public UserState state;    
    
    public abstract void setState(UserState state);    

    public abstract UserState getState();    

    public abstract AsteriskChannel getChannel();

    public abstract void setChannel(AsteriskChannel channel);

    public abstract String getName();    

    public abstract void onNewAsteriskChannel(AsteriskChannel channel);
}
