/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.agent;

import org.asteriskjava.live.AsteriskChannel;


public class User {

    private String callerId;
    private String parkingLotNo;
    private String channelId;
    private AsteriskChannel channel;

    public String getParkingLotNo() {
        return parkingLotNo;
    }

    public void setParkingLotNo(String parkingLotNo) {
        this.parkingLotNo = parkingLotNo;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public AsteriskChannel getChannel() {
        return channel;
    }

    public void setChannel(AsteriskChannel channel) {
        this.channel = channel;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
