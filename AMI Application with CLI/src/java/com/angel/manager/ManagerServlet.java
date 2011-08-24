/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.manager;

import com.angel.agent.states.TalkingToSuperVisorState;
import com.angel.admin.states.SuperVisorSpyingState;
import com.angel.agent.states.ParkedCallState;
import com.angel.base.State;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author prashanth_p
 */
public class ManagerServlet extends HttpServlet {
    
    //static ManagerServer manager;
    static boolean start = false;
/*
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String actionValue = request.getParameter("actionValue");            
            System.out.print("Action value " + actionValue);
            if (actionValue != null) {
                if (actionValue.equals("Login")) {                    
                        login(request);                                                
                } else if (actionValue.equals("Park")) {
                    System.out.println("Trying to park the call");
                    park(request);
                    
                } else if (actionValue.equals("Bridge")) {
                    bridge(request);
                } else if (actionValue.equals("Join")) {
                    join(request);
                } else if (actionValue.equals("Super")) {
                    supervise(request);
                } else if (actionValue.equals("Exit")) {
                    exit(request);
                } else {
                    System.out.println("Not supported yet");
                }
            }else{
                request.setAttribute("status", "Action not recognized");
            }

        } catch (Exception e) {
            request.setAttribute("status","You need to login first");
        } finally {
            String destination = "/AMI_Interface.jsp";
            RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
            rd.forward(request, response);
            out.close();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  /*  @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void login(HttpServletRequest request) throws IOException {
        try {
            if (!start) {
                //ManagerInstance.manager = new ManagerServer();
                ManagerInstance.manager.run();
                start = true;
                request.setAttribute("status", "Successfully logged in to Asterisk server");
            } else {
                request.setAttribute("status", "Already logged in to Asterisk Server");
            }
        } catch (TimeoutException ex) {
            Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ManagerCommunicationException ex) {
            Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/*
    private void park(HttpServletRequest request) {
        System.out.print("Doing parking");
        if ((ManagerInstance.manager != null) && !ManagerInstance.manager.getCurrentState().toString().contains("InitialState") && ManagerInstance.manager.getAgentChannel().getState() == State.ESTABLISHED) {
            try {
                ManagerInstance.manager.parkCall(ManagerInstance.manager);
                request.setAttribute("status", "Parked the call");
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(ManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            request.setAttribute("status", "Call is not established between user and agent");
        }
        System.out.print("Done with parking");
    }

    private void bridge(HttpServletRequest request) {
        System.out.print("Bridging the call");
        if ((ManagerInstance.manager != null) && ManagerInstance.manager.getCurrentState().toString().contains("EstablishedState")) {
            if (ManagerInstance.manager.getAgentChannel().getState() == State.HANGUP) {
                ParkedCallState parkState = new ParkedCallState();
                ManagerInstance.manager.changeState(parkState);
                parkState.callToSu(ManagerInstance.manager);
                request.setAttribute("status", "Bingo!! Agtent now talking to Supervisor");
            } else {
                request.setAttribute("status", "Agent not hungup..can't bridge");
            }
        } else {
            request.setAttribute("status", "You need to park the call first");
        }
        System.out.print("Done with the bridging");
    }

    private void join(HttpServletRequest request) {
        System.out.print("Doing join conference");
        if ((ManagerInstance.manager != null) && (ManagerInstance.manager.getCurrentState().toString().contains("ParkedCallState"))) {
            if (ManagerInstance.manager.getSuperVisorChannel().getState() == State.ESTABLISHED) {
                ManagerInstance.manager.changeState(new TalkingToSuperVisorState());
                ManagerInstance.manager.redirectToConference(ManagerInstance.manager);
                request.setAttribute("status", "Agent,User and Supervisor talking to each other in conference");
                System.out.println("Going to put all in conference");
            }
        } else {
            request.setAttribute("status", "Bridge the call first");
        }
        System.out.print("Done with join conference");
    }

    private void supervise(HttpServletRequest request) {
        System.out.print("Supervising the call");
        if (!(ManagerInstance.manager == null) && ManagerInstance.manager.getAgentChannel().getState() == State.ESTABLISHED) {
            ManagerInstance.manager.changeState(new SuperVisorSpyingState());
            ManagerInstance.manager.channelSpy(ManagerInstance.manager);
            request.setAttribute("status", "Supervisor is supervising the agent");
        } else {
            request.setAttribute("status", "Still call is not established between user and agent");
        }
        System.out.print("Done with Supervising");
    }

    private void exit(HttpServletRequest request) {
        System.exit(0);
    }
*/
}
