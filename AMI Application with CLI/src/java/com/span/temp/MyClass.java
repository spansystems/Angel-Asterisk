/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.temp;

import javax.swing.event.EventListenerList;
import org.asteriskjava.live.AsteriskChannel;

/**
 *
 * @author prashanth_p
 */
public class MyClass {

    protected EventListenerList listenerList = new EventListenerList();

    // This methods allows classes to register for MyEvents
    public void addMyEventListener(MyEventListener listener) {
        listenerList.add(MyEventListener.class, listener);
    }

    // This methods allows classes to unregister for MyEvents
    public void removeMyEventListener(MyEventListener listener) {
        listenerList.remove(MyEventListener.class, listener);
    }

    // This private class is used to fire MyEvents
    void fireMyEvent(MyEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == MyEventListener.class) {
                ((MyEventListener) listeners[i + 1]).onMyEvent(evt);
            }
        }
    }

    public static void main(String[] args) {
        MyClass c = new MyClass();
// Register for MyEvents from c
        c.addMyEventListener(new MyEventListener() {
            public void onMyEvent(MyEvent evt) {
                System.out.println("The source of the event is =>"+((AsteriskChannel)evt.getSource()).getCallerId());
            }
        } );
        c.fireMyEvent(new MyEvent(c));
    }
}
