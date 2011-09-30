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
public class ScaleCommand extends Command  {
    private static Integer[]           scales = {25,50,75,100,125,150,175,200,250,300};

    public ScaleCommand() {
        int scale = getPreferenceValue();
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

    private JPanel createScalePane(final JDialog dialog) {
        JPanel   p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder( BorderFactory.createTitledBorder("Projection Scale") );

        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int percentage = Integer.parseInt( e.getActionCommand() );
                dialog.dispose();
                setPreferenceValue(percentage);
                updateView(percentage);
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

}
