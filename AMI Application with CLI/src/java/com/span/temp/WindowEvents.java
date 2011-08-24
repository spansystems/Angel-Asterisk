/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.temp;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author prashanth_p
 */
public class WindowEvents extends Applet
        implements MouseListener, MouseMotionListener {

    SampleFrame f;
    String msg = "";
    int mouseX = 0, mouseY = 10;
    int movX = 0, movY = 0;
// Create a frame window.
    @Override
    public void init() {
        f = new SampleFrame("Handle Mouse Events");
        f.setSize(300, 200);
        f.setVisible(true);
// register this object to receive its own mouse events
        addMouseListener(this);
        addMouseMotionListener(this);
    }
// Remove frame window when stopping applet.
    @Override
    public void stop() {
        f.setVisible(false);
    }
// Show frame window when starting applet.
    @Override
    public void start() {
        f.setVisible(true);
    }
// Handle mouse clicked.
    public void mouseClicked(MouseEvent me) {
    }
// Handle mouse entered.
    public void mouseEntered(MouseEvent me) {
// save coordinates
        mouseX = 0;
        mouseY = 24;
        msg = "Mouse just entered applet window.";
        repaint();
    }
// Handle mouse exited.
    public void mouseExited(MouseEvent me) {
// save coordinates
        mouseX = 0;
        mouseY = 24;
        msg = "Mouse just left applet window.";
        repaint();
    }
// Handle button pressed.
    public void mousePressed(MouseEvent me) {
// save coordinates
        mouseX = me.getX();
        mouseY = me.getY();
        msg = "Down";
        repaint();
    }
// Handle button released.
    public void mouseReleased(MouseEvent me) {
// save coordinates
        mouseX = me.getX();
        mouseY = me.getY();
        msg = "Up";
        repaint();
    }
// Handle mouse dragged.
    public void mouseDragged(MouseEvent me) {
// save coordinates
        mouseX = me.getX();
        mouseY = me.getY();
        movX = me.getX();
        movY = me.getY();
        msg = "*";
        repaint();
    }
// Handle mouse moved.
    public void mouseMoved(MouseEvent me) {
// save coordinates
        movX = me.getX();
        movY = me.getY();
        repaint(0, 0, 100, 20);
    }
// Display msg in applet window.
    @Override
    public void paint(Graphics g) {
        g.drawString(msg, mouseX, mouseY);
        g.drawString("Mouse at " + movX + ", " + movY, 0, 10);
    }
}