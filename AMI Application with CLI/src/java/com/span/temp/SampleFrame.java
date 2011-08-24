/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.temp;

/**
 *
 * @author prashanth_p
 */
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
/*
<applet code="WindowEvents" width=300 height=50>
</applet>
- 474 -
 */
// Create a subclass of Frame.
class SampleFrame extends Frame
        implements MouseListener, MouseMotionListener {

    String msg = "";
    int mouseX = 10, mouseY = 40;
    int movX = 0, movY = 0;

    SampleFrame(String title) {
        super(title);
// register this object to receive its own mouse events
        addMouseListener(this);
        addMouseMotionListener(this);
// create an object to handle window events
        MyWindowAdapter adapter = new MyWindowAdapter(this);
// register it to receive those events
        addWindowListener(adapter);
    }
// Handle mouse clicked.
    public void mouseClicked(MouseEvent me) {
    }
// Handle mouse entered.
    public void mouseEntered(MouseEvent evtObj) {
// save coordinates
        mouseX = 10;
        mouseY = 54;
        msg = "Mouse just entered child.";
        repaint();
    }
// Handle mouse exited.
    public void mouseExited(MouseEvent evtObj) {
// save coordinates
        mouseX = 10;
        mouseY = 54;
        msg = "Mouse just left child window.";
        repaint();
    }
// Handle mouse pressed.
    public void mousePressed(MouseEvent me) {
// save coordinates
        mouseX = me.getX();
        mouseY = me.getY();
        msg = "Down";
        repaint();
    }
// Handle mouse released.
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
        repaint(0, 0, 100, 60);
    }

    @Override
    public void paint(Graphics g) {
        g.drawString(msg, mouseX, mouseY);
        g.drawString("Mouse at " + movX + ", " + movY, 10, 40);
    }
}


// Applet window.
