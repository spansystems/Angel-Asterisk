package com.span.asterisk.ami;
import org.asteriskjava.live.AsteriskChannel;
public class ManagerChannel {
	private String name;
	private ManagerChannel peerChannel;
	private State state;
	private AsteriskChannel channel;
	
	public ManagerChannel getPeerChannel() {
		return peerChannel;
	}

	public void setPeerChannel(ManagerChannel peerChannel) {
		this.peerChannel = peerChannel;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public AsteriskChannel getChannel() {
		return channel;
	}

	public void setChannel(AsteriskChannel channel) {
		this.channel = channel;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
