/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.base;

import org.asteriskjava.live.AsteriskChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IAgent
{

	public static final Logger LOG = LoggerFactory.getLogger(IAgent.class);
	public String name;
	public AsteriskChannel channel;
	public UserState state;

	public abstract void setState(UserState state);

	public abstract UserState getState();

	public abstract AsteriskChannel getChannel();

	public abstract void setChannel(AsteriskChannel channel);

	public abstract String getName();

	public abstract void onNewAsteriskChannel(AsteriskChannel channel);
}
