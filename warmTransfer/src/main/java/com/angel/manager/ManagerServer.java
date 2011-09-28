/*
 * This class handles the connection with Asterisk server and also manages Agents and Admin logins
 * and their functionalities.
 * It also contains event listner implementation and hence is the core class for event handling.
 */
package com.angel.manager;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.agent.User;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.event.NewChannelEvent;

public final class ManagerServer implements AsteriskServerListener, ManagerEventListener {

    private AsteriskServer asteriskServer;
    static ManagerConnection managerConnection;
    Agent agent;
    Admin admin;
    boolean quit = false;
    static final Logger logger = Logger.getLogger(ManagerServer.class);
    private String Asterisk_IP;
    private String loginName;
    private String loginPwd;

    /*
     * Constructor is used to initate the connection with asterisk server with ip, username and passwd.
     * Agent and Admin are instantiazed at this point.
     */
    ManagerServer() {
        readFileForIP();
        asteriskServer = new DefaultAsteriskServer(Asterisk_IP, loginName, loginPwd);
        managerConnection = asteriskServer.getManagerConnection();
    }
    /*
     * Adding the listeners to this class for asterisk events and status events.
     */

    public void run() throws ManagerCommunicationException, IllegalArgumentException, IllegalStateException, IOException,
            TimeoutException {
        try {
            asteriskServer.addAsteriskServerListener(this);
            managerConnection.addEventListener(this);
            StatusAction action = new StatusAction();
            managerConnection.sendAction(action);
            Thread.sleep(300000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ManagerServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /*
     * Receives  NewAsteriskChannel channel from Asterisk server.
     */

    @Override
    public void onNewAsteriskChannel(AsteriskChannel channel) {
        logger.info("Asterisk Channel received: " + channel);
        String channelId = channel.getId();
        Agent userAgent = getUserAgentForTheChannel(channelId);
        if (userAgent != null) {
            userAgent.onNewAsteriskChannel(channel);
        } else {
            Admin localAdmin = getAdminChannelID(channelId);
            if (null != localAdmin) {
                localAdmin.onNewAsteriskChannel(channel);
                logger.info("In Admin new asterisk channel");
            } else {
                logger.info("Asterisk channel Unidentified:" + channel);
            }
        }
    }

    public static ManagerConnection getManagerConnection() {
        return managerConnection;
    }

    public static void setManagerConnection(ManagerConnection managerconnection) {
        managerConnection = managerconnection;
    }
    /*
     * Starting point for creating the connection with asteisk server
     */

    public static void main(String[] args) throws Exception {
        ManagerServer manager = new ManagerServer();
        manager.run();
    }
    /*
     * Receiving the manager events and filtering for new channel events.
     * Storing the channel id's for user, agent and Admin.
     * Corresponding events are sent to the respective handlers.
     */

    @Override
    public void onManagerEvent(ManagerEvent event) {
        {
            String eventType = event.getClass().toString();
            if (eventType.contains("NewChannelEvent")) {
                onNewChannelEvent(event);
            } else if (eventType.contains("NewCallerIdEvent")) {
                onNewCallerIdEvent(event);
                logger.info("Received NewCallerIdEvent event " + event);
            } else if (eventType.contains("DialEvent")) {
                onDialEvent(event);
                //logger.info("Received Dial event " + event);
            } else if (eventType.contains("BridgeEvent")) {
                onBridgeEvent(event);
                logger.info("Received Bridge Event " + event);
            } else if (eventType.contains("HangupEvent")) {
                onHangupEvent(event);
                logger.info("Received Hangup Event " + event);
            } else if (eventType.contains("MeetMeJoinEvent")) {
                onMeetMeJoinEvent(event);
                logger.info("Received MeetMeJoin Event " + event);
            } else if (eventType.contains("MeetMeLeaveEvent")) {
                onMeetMeLeaveEvent(event);
                logger.info("Received MeetMeLeave Event " + event);
            } else if (eventType.contains("NewStateEvent")) {
                onNewStateEvent(event);
                logger.info("Received NewState Event " + event);
            } else if (eventType.contains("ParkedCallEvent")) {
                onParkedCallEvent(event);
                logger.info("Received ParkedCall Event " + event);
            } else if (eventType.contains("RegistryEvent")) {
                onRegistryEvent(event);
                logger.info("Received Registry Event " + event);
            } else if (eventType.contains("OriginateSuccessEvent")) {
                onOriginateSuccessEvent(event);
                logger.info("Received OriginateSuccess Event " + event);
            } else {
                logger.info("The event received is not handled at this point and is:" + event);
            }
        }

    }

    /*
     * Checks and returns the instance Agent(only) for Asterisk Channel event received.
     */
    public Agent getUserAgentForTheChannel(String id) {
        Agent agentTemp = AgentMap.getAgentById(id);
        if (null != agentTemp) {
            logger.info("Returning the agent for the channel id " + id);
            return agentTemp;
        } else {
            return null;
        }
    }
    /*
     * Returns admin instance according as the Asterisk channel received.
     */

    public Admin getAdminChannelID(String id) {
        Admin adminTemp = AdminMap.getAdminById(id);
        if (null != adminTemp) {
            return adminTemp;
        } else {
            return null;
        }
    }
    /*
     * Called from onNewChannelEvent.
     * Sets the peerchannel id of agent for the respective user.
     * For others channel id's are set in the same method.
     */

    private void setUserInfo(NewChannelEvent channel) {
        String extension = channel.getExten();
        if (AgentMap.checkAgentExist(extension)) {
            Agent agentToContact = AgentMap.getAgent(extension);
//            agentToContact.setPeerChannelId(channel.getUniqueId());
            if (agentToContact.getUser() == null) {
                agentToContact.setUser(new User());
            }
            agentToContact.getUser().setChannelId(channel.getUniqueId());
            agentToContact.getUser().setCallerId(channel.getCallerId());
            logger.info("Received the user manager channel event");
        } else {
            logger.info("Agent, user trying to call doen't exist");
        }
    }
    /*
     * Receives the events from ManagerEvent method after getting filtered for new channel event
     * It Initialized the channel id's for user,agent and superuser.
     * These id's are used on New Asteisk channel event to recognize user admin or superuser.
     */

    private void onNewChannelEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
        NewChannelEvent channel = (NewChannelEvent) event;        
        if (null != channel.getCallerIdName() && null != channel.getExten()) {
            if (!AgentMap.checkAgentExist(channel.getExten())) {
                AgentMap.setAgent(channel.getExten(), new Agent(channel.getExten()));
            }
            setUserInfo(channel);
        } else {
            logger.info("New Channel event received which is not of user:" + event);
        }
    }

    /*
     * Handles dial event
     */
    private void onDialEvent(ManagerEvent event) {
        logger.info("Dial channel event received: " + event);
        DialEvent channel = (DialEvent) event;
        if (null != channel.getDialString()) {
            String dialedString = channel.getDialString().replace("@out", "");
            if (AgentMap.checkAgentExist(dialedString)) {
                Agent agentReceived = AgentMap.getAgent(dialedString);
                agentReceived.setChannelId(channel.getDestUniqueId());
                logger.info("Received an agent dial event " + agentReceived);
            } else if (AdminMap.checkAdminExist(dialedString) && AgentMap.checkAgentExist(channel.getCallerId())) {
                logger.info("Admin channel event, setting the channel in the manager agent " + channel);
                Admin localadmin = AdminMap.getAdmin(dialedString);
                localadmin.setChannelId(channel.getDestUniqueId());
                AgentMap.getAgent(channel.getCallerId()).setChannelId(channel.getSrcUniqueId());
            } else {
                logger.info("UnIdentified New CallerID event:" + event);
            }
        } else {
            logger.info("Unknown dial event" + channel);
        }

    }

    private void onBridgeEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
        BridgeEvent channel = (BridgeEvent) event;
        Agent agentWhoPickedUser = AgentMap.getAgent(channel.getCallerId1());
        if (null != agentWhoPickedUser && agentWhoPickedUser.getState().toString().contains("JoinConferenceState")) {
            agentWhoPickedUser.setChannelId(channel.getUniqueId1());
        }
    }

    private void onHangupEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
    }

    private void onMeetMeJoinEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
    }

    private void onMeetMeLeaveEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
        //MeetMeLeaveEvent channel = (MeetMeLeaveEvent) event;        

    }

    private void onNewStateEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
        //NewStateEvent channel = (NewStateEvent) event;        
    }
    /*
     * Handles ParkedCallEvent.
     * Controls the agents established state and is used to check whether user is parked or not.
     */

    private void onParkedCallEvent(ManagerEvent event) {
        logger.info("Parked  channel event received: " + event);
        ParkedCallEvent channel = (ParkedCallEvent) event;
        String userCallerId = channel.getCallerId();
        Agent agentConnectedTo = AgentMap.getAgentByUser(userCallerId);
        if (null != agentConnectedTo) {
            ((com.angel.agent.states.EstablishedState) agentConnectedTo.getState()).processParkedUser();
            agentConnectedTo.getUser().setParkingLotNo(channel.getExten());
        }
    }

    private void onRegistryEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
        //RegistryEvent channel = (RegistryEvent) event;        
    }

    private void onOriginateSuccessEvent(ManagerEvent event) {
        logger.info("Manager new channel event received: " + event);
        //OriginateResponseEvent channel = (OriginateResponseEvent) event;        
    }

    private void onNewCallerIdEvent(ManagerEvent event) {
        logger.info("New caller Id event received " + event);
    }

    public void onNewMeetMeUser(MeetMeUser mmu) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onNewAgent(AsteriskAgentImpl aai) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onNewQueueEntry(AsteriskQueueEntry aqe) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void readFileForIP() {
        try {
            final Properties props = ManagerServer.loadProperties(ManagerServer.class, "conf.properties");
            Asterisk_IP = props.getProperty("ip");
            loginName = props.getProperty("username");
            loginPwd = props.getProperty("pwd");
        } catch (IOException ex) {
            logger.error("IO Exception while reading the configuration file.", ex);
        }
    }

    public static Properties loadProperties(final Class<?> clazz, final String resourceName) throws IOException {
        final Properties props = new Properties();
        InputStream inStream = null;

        try {
            inStream = clazz.getResourceAsStream(resourceName);
            if (inStream == null) {
                throw new IOException("Error while loading " + resourceName + ": resource not found in the JAR file");
            }
            if (resourceName.endsWith(".properties")) {
                props.load(inStream);
            } else {
                throw new IOException("Property file " + resourceName + " must end with .xml or .properties");
            }
            return props;
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
}
