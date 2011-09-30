package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class FrameRateCommand extends Command {
    private Integer[] updatesPerMinute = {1, 15, 30, 60, 100};

    public FrameRateCommand() {
        updateView(getRate());
        setTooltip("Sets the rate of how many screen-shots should be taken per second");
        setIcon("rate");
    }

    public void updateView(int rate) {
        setLabel(String.format("Frame Rate (%d frames/min)", rate));
    }

    @Override
    protected void doExecute(Application app) {
        JDialog dialog = new JDialog(app.getAppFrame(), "Set Frame Rate", true);
        JOptionPane optPane = new JOptionPane(createPane(dialog), JOptionPane.QUESTION_MESSAGE);
        dialog.setContentPane(optPane);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private JPanel createPane(final JDialog dialog) {
        JPanel   panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Frame Rate (frames / minute)"));

        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rate = Integer.parseInt( e.getActionCommand() );
                dialog.dispose();
                setPreferenceValue(rate);
                updateView(rate);
                getApplication().setFrameRate(rate);
            }
        };

        ButtonGroup     group = new ButtonGroup();
        for (int count : updatesPerMinute) {
            JRadioButton rb = createButton(count, action);
            group.add(rb);
            panel.add(rb);
        }

        return panel;
    }

    private JRadioButton    createButton(int numUpdates, ActionListener action) {
        String lbl = Integer.toString(numUpdates);

        JRadioButton r = new JRadioButton(numUpdates == 100 ? "Fastest" : lbl);
        r.setActionCommand(lbl);
        r.addActionListener(action);
        if (numUpdates == getPreferenceValue()) {
            r.setSelected(true);
        }

        return r;
    }

    protected String getPreferencesKey() {
        return "frame-rate";
    }

    protected void setPreferenceValue(int value) {
        getApplication().getPreferences().putInt(getPreferencesKey(), value);
        getApplication().savePreferences();
    }

    protected int getPreferenceValue() {
        return getApplication().getPreferences().getInt(getPreferencesKey(), 1);
    }

    public int getRate() {
        return getPreferenceValue();
    }

}
