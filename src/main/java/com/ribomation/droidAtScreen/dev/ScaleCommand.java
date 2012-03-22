/*
 * Project:  droidAtScreen
 * File:     ScaleCommand.java
 * Modified: 2012-03-22
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.dev;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.cmd.CommandWithTarget;
import com.ribomation.droidAtScreen.gui.DeviceFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Set the device frame projection scale, as a percentage.
 * <p/>
 * User: Jens
 * Created: 2012-03-22, 22:18
 */
public class ScaleCommand extends CommandWithTarget<DeviceFrame> {

    public ScaleCommand(DeviceFrame deviceFrame) {
        super(deviceFrame);
        setIcon("zoom");
        setTooltip("Sets the projection scale % of the Android Device. 100% is normal size");
        setMnemonic('Q');
        updateLabel(deviceFrame);
    }

    @Override
    protected void doExecute(Application app) {
        final DeviceFrame   deviceFrame = getTarget();
        final JDialog       dialog = new JDialog(app.getAppFrame(), "Set the Device Frame Scale", true);

        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                int percentage = Integer.parseInt( e.getActionCommand() );
                deviceFrame.setScale(percentage);
                updateLabel(deviceFrame);
                deviceFrame.validate();
            }
        };

        JPanel scalePane = createScalePane(action);
        dialog.setContentPane(new JOptionPane(scalePane, JOptionPane.QUESTION_MESSAGE));
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private void updateLabel(DeviceFrame deviceFrame) {
        setLabel(String.format("Scale (%d%%)", deviceFrame.getScale()));
    }

    @Override
    public AbstractButton newButton() {
        JToggleButton b = new JToggleButton(this);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setSelected(true);
        return b;
    }

    private JPanel createScalePane(ActionListener action) {
        JPanel   p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder( BorderFactory.createTitledBorder("Projection Scale") );
        ButtonGroup     scale = new ButtonGroup();
        for (int s : getApplication().getSettings().getScales()) {
            JRadioButton rb = createScaleRadioButton(s, action);
            scale.add(rb);
            p.add(rb);
        }
        return p;
    }

    private JRadioButton    createScaleRadioButton(int percentage, ActionListener action) {
        JRadioButton   r = new JRadioButton(percentage + "%");
        r.setActionCommand( Integer.toString(percentage) );
        r.addActionListener(action);
        r.setSelected(percentage == getTarget().getScale());
        return r;
    }
    
}
