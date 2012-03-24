/*
 * Project:  droidAtScreen
 * File:     ScaleCommand.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Set the device frame projection scale, as a percentage.
 *
 * @user jens
 * @date 30 september 2011, 14:19
 */
@Deprecated
public class ScaleCommand extends Command  {

    public ScaleCommand() {
        int scale = getApplication().getSettings().getScale();
        updateView(scale);
        setIcon("zoom");
        setTooltip("Sets the projection scale % of the Android Device. 100% is normal size");
        setMnemonic('Q');
        setEnabledOnlyWithDevice(true);
    }

    private void updateView(int scale) {
        setLabel(String.format("Scale (%d%%)", scale));
    }

    @Override
    protected void doExecute(Application app) {
        JDialog dialog = new JDialog(app.getAppFrame(), "Set the Device Frame Scale", true);
        JOptionPane optPane = new JOptionPane(createScalePane(dialog), JOptionPane.QUESTION_MESSAGE);
        dialog.setContentPane(optPane);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private JPanel createScalePane(final JDialog dialog) {
        JPanel   p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder( BorderFactory.createTitledBorder("Projection Scale") );

        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int percentage = Integer.parseInt( e.getActionCommand() );
                dialog.dispose();
                getApplication().getSettings().setScale(percentage);
                updateView(percentage);
                getApplication().setScale(percentage);
            }
        };

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
        if (percentage == getApplication().getSettings().getScale()) {
            r.setSelected(true);
        }
        return r;
    }

}
