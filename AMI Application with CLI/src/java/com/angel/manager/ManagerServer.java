/*
 * This class handles the connection with Asterisk server and also manages Agents and Admin logins
 * and their functionalities.
 * It also contains event listner implementation and hence is the core class for event handling.
 */
package com.angel.manager;

import com.angel.admin.states.SuperVisorSpyingState;
import com.angel.agent.Admin;
import com.angel.agent.Agent;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JOptionPane;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.ChannelState;
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
    private static final String userName = "100";
    private static final String agentName = "200";
    private static final String suName = "300";
    private final String agentPwd = "span";
    private final String adminPwd = "span@1234";
    Agent agent;
    Admin admin;
    boolean quit = false;
    private static HashMap<String, Admin> adminMap = new HashMap<String, Admin>();

    public static String getAgentName() {
        return agentName;
    }

    public static String getSuName() {
        return suName;
    }

    public static String getUserName() {
        return userName;
    }

    public static Admin getAdmin(String name) {
        if (adminMap.containsKey(name)) {
            return adminMap.get(name);
        } else {
            return null;
        }
    }

    public static void setAdmin(String key, Admin value) {
        ManagerServer.adminMap.put(key, value);
    }
    /*
     * Constructor is used to initate the connection with asterisk server with ip, username and passwd.
     * Agent and Admin are instantiazed at this point.
     */

    ManagerServer() {
        asteriskServer = new DefaultAsteriskServer("IP_TO_BE_ENTERED", "admin", "span");
        managerConnection = asteriskServer.getManagerConnection();
        admin = new Admin();
        admin.setName(suName);
        setAdmin(suName, admin);
        agent = new Agent();
        agent.setName(agentName);
        Admin.setAgent(agentName, agent);
    }
    /*
     * Adding the listeners to this class for asterisk events and status events.
     */

    public void run() throws ManagerCommunicationException, IllegalArgumentException, IllegalStateException, IOException,
            TimeoutException {

        asteriskServer.addAsteriskServerListener(this);
        managerConnection.addEventListener(this);
        StatusAction action = new StatusAction();
        managerConnection.sendAction(action);
        //Thread.sleep(300000);            
        readInput();

    }
    /*
     * User input for role is handled here.
     */

    private void readInput() {
        String whoAreYou = null;
        while (!"bye".equals(whoAreYou)) {
            whoAreYou = JOptionPane.showInputDialog("Who are you??(Type exit to Exit)");
            String gotIt = findWhoAreYou(whoAreYou);
            if (gotIt.contains("agent")) {
                processAgent(gotIt);
            } else if (gotIt.contains("admin")) {
                processAdmin(gotIt);
            } else if (whoAreYou.contains("exit")) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, "UnKnown");
            }
        }

    }
    /*
     * Receives  NewAsteriskChannel channel from Asterisk server.
     */

    @Override
    public void onNewAsteriskChannel(AsteriskChannel channel) {
        System.out.println("Asterisk Channel received: " + channel);
        String channelId = channel.getId();
        Agent userAgent = getUserAgentForTheChannel(channelId);
        if (userAgent != null) {
            userAgent.onNewAsteriskChannel(channel);
        } else {
            Admin localAdmin = getAdminChannelID(channelId);
            if (localAdmin != null) {
                localAdmin.onNewAsteriskChannel(channel);
            }
        }
    }

    public static ManagerConnection getManagerConnection() {
        return managerConnection;
    }

    public static void setManagerConnection(ManagerConnection managerconnection) {
        managerConnection = managerconnection;
    }

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
            //event.
            String eventType = event.getClass().toString();
            if (eventType.contains("NewChannelEvent")) {
                onNewChannelEvent(event);
            } else if (eventType.contains("DialEvent")) {
                onDialEvent(event);
                System.out.println("Received Dial event " + event);
            } else if (eventType.contains("BridgeEvent")) {
                onBridgeEvent(event);
                System.out.println("Received Bridge Event " + event);
            } else if (eventType.contains("HangupEvent")) {
                onHangupEvent(event);
                System.out.println("Received Hangup Event " + event);
            } else if (eventType.contains("MeetMeJoinEvent")) {
                onMeetMeJoinEvent(event);
                System.out.println("Received MeetMeJoin Event " + event);
            } else if (eventType.contains("MeetMeLeaveEvent")) {
                onMeetMeLeaveEvent(event);
                System.out.println("Received MeetMeLeave Event " + event);
            } else if (eventType.contains("NewStateEvent")) {
                onNewStateEvent(event);
                System.out.println("Received NewState Event " + event);
            } else if (eventType.contains("ParkedCallEvent")) {
                onParkedCallEvent(event);
                System.out.println("Received ParkedCall Event " + event);
            } else if (eventType.contains("RegistryEvent")) {
                onRegistryEvent(event);
                System.out.println("Received Registry Event " + event);
            } else if (eventType.contains("OriginateSuccessEvent")) {
                onOriginateSuccessEvent(event);
                System.out.println("Received OriginateSuccess Event " + event);
            } else {
                System.out.println("The event received is not handled at this point and is:" + event);
            }
        }

    }

    //@Override
    public void onNewAgent(AsteriskAgentImpl arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNewMeetMeUser(MeetMeUser arg0) {
        // TODO Auto-generated method stub
    }

    //@Override
    public void onNewQueueEntry(AsteriskQueueEntry arg0) {
        // TODO Auto-generated method stub
    }
    /*
     * Checks and returns the instance Agent(only) for Asterisk Channel event received.
     */

    public Agent getUserAgentForTheChannel(String id) {
        Agent agentTemp = Admin.getAgent(agentName);
        if (agentTemp.getPeerChannelId().equals(id) || agentTemp.getChannelId().equals(id)) {
            System.out.println("Returning the agent for the channel id " + id);
            return agentTemp;
        } else {
            return null;
        }
    }
    /*
     * Returns admin instance according as the Asterisk channel received.
     */

    public Admin getAdminChannelID(String id) {
        Admin adminTemp = ManagerServer.getAdmin(suName);
        if (adminTemp == null) {
            return null;
        } else {
            return admin;
        }
    }
    /*
     * Returns UserName, Agent Name or Super User name to onNewChannelEvent method.
     */

    private String getEventOwnerName(String channel) {
        if (channel.contains(userName)) {
            System.out.println("Returning the user " + userName);
            return userName;
        } else if (channel.contains(agentName)) {
            System.out.println("Returning the agent " + agentName);
            return agentName;
        } else if (channel.contains(suName)) {
            System.out.println("Returning the admin " + suName);
            return suName;
        } else {
            return "unKnown";
        }
    }
    /*
     * Called from onNewChannelEvent.
     * Sets the peerchannel id of agent for the respective user.
     * For others channel id's are set in the same method.
     */

    private void setUserInfo(NewChannelEvent channel) {
        String extension = channel.getExten();
        if (extension != null) {
            if (Admin.checkAgentExist(extension)) {
                Agent agentToContact = Admin.getAgent(extension);
                agentToContact.setPeerChannelId(channel.getUniqueId());
                System.out.println("Received the user manager channel event");
            } else {
                System.out.println("Agent doen't exist");
            }
        } else {
            System.out.println("Recieved channel without extension");
        }
    }
    /*
     * Receives the events from ManagerEvent method after getting filtered for new channel event
     * It Initialized the channel id's for user,agent and superuser.
     * These id's are used on New Asteisk channel event to recognize user admin or superuser.
     */

    private void onNewChannelEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        NewChannelEvent channel = (NewChannelEvent) event;
        String name = getEventOwnerName(channel.getChannel());
        if (name.equals(userName)) {
            setUserInfo(channel);
        } else if (name.equals(agentName)) {
            Agent agentReceived = Admin.getAgent(name);
            System.out.println("Agent received is :" + agentReceived);
            if (agentReceived != null) {
                agentReceived.setChannelId(channel.getUniqueId());
                System.out.println("Received an agent channel event " + agentReceived);
            } else {
                System.out.println("Agent unknown");
            }
        } else if (name.equals(suName)) {
            System.out.println("Supervisor channel event, setting the channel in the manager agent " + channel);
            Admin localadmin = ManagerServer.getAdmin(suName);
            if (localadmin != null) {
                localadmin.setChannelId(channel.getUniqueId());
            }
        } else {
            System.out.println("Unknown channel");
        }
    }
    /*
     * Processes the CLI for agent asking for different actions in a loop.
     * Perforams specific action provided by agent.
     */

    private void processAgentCLI(Agent agent) {
        try {
            String input = "hello";
            while (!input.equals("exit")) {
                input = JOptionPane.showInputDialog("What do you want to do??1. park 2.supervise 3.bridge 4.state 5.exit");
                //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                //= br.readLine();			
                if (input.contains("park")) {
                    if (agent.getState().toString().contains("EstablishedState") && agent.getChannel().getState() == ChannelState.UP) {
                        agent.getState().doParkCall(agent);
                    } else {
                        JOptionPane.showMessageDialog(null, "Still the call is not established between user and agent");
                    }
                } else if (input.contains("supervise")) {
                    if (agent.getState().toString().contains("ParkedCallState") && agent.getChannel() == null) {
                        agent.getState().callToSu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Still the call is not established between user and agent");
                    }
                } else if (input.contains("bridge")) {
                    //System.out.println(this.getCurrentState().toString());
                    if (agent.getState().toString().contains("TalkingToSuperVisorState") && agent.getAdmin().getChannel().getState() == ChannelState.UP) {
                        agent.getState().redirectToConference(agent);
                    } else {
                        JOptionPane.showMessageDialog(null, "Wait!! Let the agent Hangup");
                    }
                } else if (input.contains("exit")) {
                    quit = true;
                    break;
                } /*else if (input.contains("events")) {
                String event = JOptionPane.showInputDialog("type \"stop\" to STOP!!");
                while (!"stop".equals(event)) {
                System.out.println("Displaying agents events");
                agent.displayEvents();
                }
                } */ else if (input.contains("state")) {
                    JOptionPane.showMessageDialog(null, "The state you are in is" + agent.getState());
                } else {
                    JOptionPane.showMessageDialog(null, "Not a valid input!!");
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown is :" + e);
        }

    }

    private String findWhoAreYou(String whoAreYou) {
        if (Admin.getAgent(whoAreYou) != null) {
            return "agent." + Admin.getAgent(whoAreYou).getName();
        } else if (ManagerServer.getAdmin(whoAreYou) != null) {
            return "admin." + ManagerServer.getAdmin(whoAreYou).getName();
        } else {
            return "unknown";
        }
    }
    /*
     * Checks for the agent's password and processes the credentials.
     * On succesfull authentication call's method for cli input.
     */

    private void processAgent(String gotIt) {
        String[] agentArray = gotIt.split("\\.");
        String agentLogin = agentArray[1];
        System.out.println(agentArray[0] + "  " + agentArray[1]);
        String agentPassword = JOptionPane.showInputDialog("Enter " + agentLogin + " password");
        if (agentPassword.equals(agentPwd)) {
            processAgentCLI(Admin.getAgent(agentLogin));
        } else {
            JOptionPane.showMessageDialog(null, "Password is wrong");
        }
    }
    /*
     * Authenticates admin and processes the CLI input.
     * Handles admin functionality.
     */

    private void processAdmin(String gotIt) {
        String[] adminArray = gotIt.split("\\.");
        String adminLogin = adminArray[1];
        String adminPassword = JOptionPane.showInputDialog("Enter " + adminLogin + " password");
        String input = "hi";
        if (adminPassword.equals(adminPwd)) {
            while (!input.equals("exit")) {
                input = JOptionPane.showInputDialog("Welcome ADMIN!! What do you want to do??"
                        + "You can do these stuffs:1.Administer agents(Type \"agent\")2.Superivise(Type \"supervise\")3.Exit");
                if (input.contains("agent")) {
                    String agentEntered = JOptionPane.showInputDialog("Enter agents name");
                    if (Admin.getAgent(agentEntered) != null) {
                        JOptionPane.showMessageDialog(null, "The state agent is in :" + Admin.getAgent(agentEntered).getState());

                    } else {
                        JOptionPane.showMessageDialog(null, "Agent entered doesn\'t exist");
                    }
                } else if (input.contains("supervise")) {
                    if (ManagerServer.getAdmin(adminLogin).getState().toString().contains("InitialState")) {
                        String agentSpy = JOptionPane.showInputDialog("Enter agent to spy");
                        if (Admin.getAgent(agentSpy) != null) {
                            SuperVisorSpyingState su = new SuperVisorSpyingState(agentSpy);
                            ManagerServer.getAdmin(adminLogin).setState(su);
                            su.channelSpy();
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }
    /*
     * Handles dial event
     */

    private void onDialEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        DialEvent channel = (DialEvent) event;
        String name = getEventOwnerName(channel.getChannel());
        processEvents(name, event);
    }

    private void onBridgeEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        BridgeEvent channel = (BridgeEvent) event;
        String name = getEventOwnerName(channel.getChannel1());
        processEvents(name, event);
    }

    private void onHangupEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        HangupEvent channel = (HangupEvent) event;
        String name = getEventOwnerName(channel.getChannel());
        processEvents(name, event);
    }

    private void onMeetMeJoinEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        MeetMeJoinEvent channel = (MeetMeJoinEvent) event;
        String name = getEventOwnerName(channel.getChannel());
        processEvents(name, event);
    }

    private void onMeetMeLeaveEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        MeetMeLeaveEvent channel = (MeetMeLeaveEvent) event;
        String name = getEventOwnerName(channel.getChannel());
        processEvents(name, event);

    }

    private void onNewStateEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        NewStateEvent channel = (NewStateEvent) event;
        String name = getEventOwnerName(channel.getChannel());
        processEvents(name, event);
    }
    /*
     * Handles ParkedCallEvent.
     * Controls the agents established state and is used to check whether user is parked or not.
     */

    private void onParkedCallEvent(ManagerEvent event) {
        System.out.println("Parked  channel event received: " + event);
        ParkedCallEvent channel = (ParkedCallEvent) event;
        String agentConnectedTo = channel.getFrom();
        if (agentConnectedTo.contains(agentName)) {
            ((com.angel.agent.states.EstablishedState) Admin.getAgent(agentName).getState()).processParkedUser();
        }
        String name = getEventOwnerName(channel.getChannel());
        processEvents(name, event);
    }

    private void onRegistryEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        RegistryEvent channel = (RegistryEvent) event;
        String name = getEventOwnerName(channel.getUsername());
        processEvents(name, event);
    }

    private void onOriginateSuccessEvent(ManagerEvent event) {
        System.out.println("Manager new channel event received: " + event);
        OriginateResponseEvent channel = (OriginateResponseEvent) event;
        String name = getEventOwnerName(channel.getChannel());
        processEvents(name, event);
    }

    private void processEvents(String name, ManagerEvent event) {

        if (name.equals(userName)) {
            Admin.getAgent(agentName).processEvents(event.toString() + "user");
        } else if (name.equals(agentName)) {
            Admin.getAgent(agentName).processEvents(event.toString() + "agent");
        } else if (name.equals(suName)) {
            Admin.getAgent(agentName).processEvents(event.toString());
        } else {
            System.out.println("Unknown user event");
        }
    }
}
