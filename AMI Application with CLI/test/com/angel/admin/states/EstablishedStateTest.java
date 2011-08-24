/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.admin.states;

import com.angel.agent.Admin;
import com.angel.agent.Agent;
import com.angel.base.UserState;
import com.angel.manager.ManagerServer;
import java.beans.PropertyChangeEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prashanth_p
 */
public class EstablishedStateTest {
    
    public EstablishedStateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of onPropertyChangeEvent method, of class EstablishedState.
     */
    @Test
    public void testOnPropertyChangeEvent_PropertyChangeEvent_Admin() throws Exception {
        System.out.println("onPropertyChangeEvent");
        PropertyChangeEvent event = null;
        Admin admin = null;
        EstablishedState instance = new EstablishedState();
        instance.onPropertyChangeEvent(event, admin);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onNewStateEvent method, of class EstablishedState.
     */
    @Test
    public void testOnNewStateEvent() {
        System.out.println("onNewStateEvent");
        NewStateEvent event = null;
        ManagerServer server = null;
        EstablishedState instance = new EstablishedState();
        instance.onNewStateEvent(event, server);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doParkCall method, of class EstablishedState.
     */
    @Test
    public void testDoParkCall() {
        System.out.println("doParkCall");
        Agent agent = null;
        EstablishedState instance = new EstablishedState();
        instance.doParkCall(agent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of redirectToConference method, of class EstablishedState.
     */
    @Test
    public void testRedirectToConference_ManagerServer() {
        System.out.println("redirectToConference");
        ManagerServer managerServer = null;
        EstablishedState instance = new EstablishedState();
        instance.redirectToConference(managerServer);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of channelSpy method, of class EstablishedState.
     */
    @Test
    public void testChannelSpy() {
        System.out.println("channelSpy");
        ManagerServer managerServer = null;
        EstablishedState instance = new EstablishedState();
        instance.channelSpy(managerServer);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setName method, of class EstablishedState.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        EstablishedState instance = new EstablishedState();
        instance.setName();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class EstablishedState.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        EstablishedState instance = new EstablishedState();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInstance method, of class EstablishedState.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        EstablishedState instance = new EstablishedState();
        UserState expResult = null;
        UserState result = instance.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onPropertyChangeEvent method, of class EstablishedState.
     */
    @Test
    public void testOnPropertyChangeEvent_PropertyChangeEvent_Agent() throws Exception {
        System.out.println("onPropertyChangeEvent");
        PropertyChangeEvent event = null;
        Agent agent = null;
        EstablishedState instance = new EstablishedState();
        instance.onPropertyChangeEvent(event, agent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of callToSu method, of class EstablishedState.
     */
    @Test
    public void testCallToSu() {
        System.out.println("callToSu");
        EstablishedState instance = new EstablishedState();
        instance.callToSu();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of redirectToConference method, of class EstablishedState.
     */
    @Test
    public void testRedirectToConference_Agent() {
        System.out.println("redirectToConference");
        Agent agent = null;
        EstablishedState instance = new EstablishedState();
        instance.redirectToConference(agent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
