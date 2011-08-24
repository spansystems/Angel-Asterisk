/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.temp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;

/**
 *
 * @author prashanth_p
 */
public class TestEvents implements AsteriskServerListener, PropertyChangeListener, ManagerEventListener{

    private AsteriskServer asteriskServer;
    ManagerConnection connection;
    
     public TestEvents() {
        asteriskServer = new DefaultAsteriskServer("10.11.201.71", "admin",
                "span");
        connection = asteriskServer.getManagerConnection();

    }    
    public void onNewAsteriskChannel(AsteriskChannel ac) {
      ac.addPropertyChangeListener(this);
      System.out.println("Received new asterisk channel event");
      System.out.println(ac);
      
    }

    public void onNewMeetMeUser(MeetMeUser mmu) {
        System.out.println("Received new meetmeuser channel event");
      System.out.println(mmu);
    }

    public void onNewAgent(AsteriskAgentImpl aai) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onNewQueueEntry(AsteriskQueueEntry aqe) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Received new propertychange channel event:-");
        System.out.println(evt);
    }

    public void onManagerEvent(ManagerEvent me) {
        NewChannelEvent event;
        System.out.println("Received new manager channel event:-");
        System.out.println(me);
        System.out.println("The manager event class name:"+me.getClass().toString());
        if(me.getClass().toString().contains("NewChannelEvent"))
        System.out.println("The channel of manager event is :"+((NewChannelEvent)me));
        
    }
    
    public static void main(String[] args) throws IOException{
        
        TestEvents test = new TestEvents();
        test.run();
    }

    private void run() throws IOException {
        try {
            asteriskServer.addAsteriskServerListener(this);
            connection.addEventListener(this);
            StatusAction action = new StatusAction();
            connection.sendAction(action);
            Thread.sleep(300000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestEvents.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(TestEvents.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestEvents.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(TestEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
