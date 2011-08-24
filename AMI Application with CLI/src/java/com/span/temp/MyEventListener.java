/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.temp;

import java.util.EventListener;

/**
 *
 * @author prashanth_p
 */
public interface MyEventListener extends EventListener{
    
    public void onMyEvent(MyEvent evt);
}
