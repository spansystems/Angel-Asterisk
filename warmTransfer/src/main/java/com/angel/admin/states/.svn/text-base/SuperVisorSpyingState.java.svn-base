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

    private String extensionName;
    private String extension;

    public SuperVisorSpyingState(final String name) {
        this.extensionName = name;
        this.extension = extensionName + ",w";
    }

    @Override
    public void onPropertyChangeEvent(final PropertyChangeEvent event, final Admin admin) throws IOException, TimeoutException {
        LOG.info("Event recieved in SuperVisorSpying State " + event);
        final AsteriskChannel channel = (AsteriskChannel) event.getSource();
        if (channel.getState() == ChannelState.HUNGUP) {
            admin.setChannel(null);
            admin.setChannelId(null);
            admin.setState(new InitialState());
        }

    }
    public void channelSpy() {
        LOG.info("Supervisor will spy the channel now");
        try {
            final OriginateAction originateUser = new OriginateAction();
            originateUser.setChannel("SIP/300");
            originateUser.setApplication("extenspy");
            originateUser.setData(extension);
            originateUser.setExten("300");
            originateUser.setPriority(1);
            ManagerServer.getManagerConnection().sendAction(originateUser);
        } catch (IllegalArgumentException e) {
            LOG.warn("Illegal argument exception");
        } catch (IllegalStateException e) {
            LOG.warn("Illegal state exception");
        } catch (IOException e) {
            LOG.warn("IO exception");
        } catch (TimeoutException e) {
            LOG.warn("Timeout exception");
        }
    }
}
