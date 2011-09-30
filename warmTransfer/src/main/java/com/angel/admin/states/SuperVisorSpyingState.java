package com.angel.admin.states;

import com.angel.agent.Admin;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;

public class SuperVisorSpyingState extends UserState {

    String extensionName;
    String extension;

    public SuperVisorSpyingState(String name) {
        this.extensionName = name;
        this.extension = extensionName + ",w";
    }

    @Override
    public void onPropertyChangeEvent(PropertyChangeEvent event, Admin admin) throws IllegalArgumentException,
            IllegalStateException, IOException, TimeoutException {
        LOG.info("Event recieved in SuperVisorSpying State " + event);
        AsteriskChannel channel = (AsteriskChannel) event.getSource();
        if (channel.getState() == ChannelState.HUNGUP) {
            admin.setChannel(null);
            admin.setChannelId(null);
            admin.setState(new InitialState());
        }

    }
    public void channelSpy() {
        System.out.println("Supervisor will spy the channel now");
        try {
            OriginateAction originateUser = new OriginateAction();
            originateUser.setChannel("SIP/300");
            originateUser.setApplication("extenspy");
            originateUser.setData(extension);
            originateUser.setExten("300");
            originateUser.setPriority(1);
            ManagerServer.getManagerConnection().sendAction(originateUser);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
        }
    }
}
