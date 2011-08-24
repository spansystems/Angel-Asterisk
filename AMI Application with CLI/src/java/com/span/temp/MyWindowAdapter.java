/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.temp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author prashanth_p
 */
class MyWindowAdapter extends WindowAdapter {

    SampleFrame sampleFrame;

    public MyWindowAdapter(SampleFrame sampleFrame) {
        this.sampleFrame = sampleFrame;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        sampleFrame.setVisible(false);
    }
}
