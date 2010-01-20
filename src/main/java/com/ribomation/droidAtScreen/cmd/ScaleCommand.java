package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class ScaleCommand extends Command {
    private static Integer[]           scales = {25,50,75,100,125,150,175,200,250,300};

    public ScaleCommand() {
        setLabel("Projection Scale");
        setTooltip("Sets the projection scale % of the Android Device. 100% is normal size");
//        setIcon("Shutdown");
//        setMnemonic('Q');
    }

    @Override
    protected void doExecute(Application app) {
        Integer percentage = (Integer) JOptionPane.showInputDialog(app.getAppFrame(),
                "Choose the projection scale %",
                "Scale %?",
                JOptionPane.QUESTION_MESSAGE,
                null,
                scales,
                getPreferenceValue());
        if (percentage == null) return;

        setPreferenceValue(percentage);
        getApplication().setScale(percentage);
    }

    protected String getPreferencesKey() {
        return "projection-scale";
    }

    protected void setPreferenceValue(int value) {
        getApplication().getPreferences().putInt(getPreferencesKey(), value);
        getApplication().savePreferences();
    }

    protected int getPreferenceValue() {
        return getApplication().getPreferences().getInt(getPreferencesKey(), 100);
    }

    public int getScale() {
        return getPreferenceValue();
    }

/*
    private JPanel createScalePane() {
        JPanel   p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder( BorderFactory.createTitledBorder("Projection Scale") );

        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int percentage = Integer.parseInt( e.getActionCommand() );
                setPreferenceValue(percentage);
                getApplication().setScale(percentage);
            }
        };

        ButtonGroup     scale = new ButtonGroup();
        for (int s : scales) {
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
        if (percentage == getPreferenceValue()) {
            r.setSelected(true);
        }
        return r;
    }
*/

}
