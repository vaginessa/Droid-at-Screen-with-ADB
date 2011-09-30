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
    private static final int FPS_MIN = 0;
    private static final int FPS_MAX = 30;
    private static final int FPS_INIT = 15;
    private JPanel ratePane;

    public FrameRateCommand() {
        setLabel("Frame Rate");
        setTooltip("Sets the rate of how many screen-shota should be taken per second");
        setIcon("rate");
    }

    @Override
    protected void doExecute(Application app) {
        if (ratePane == null) {
            ratePane = createScalePane();
        }

        JOptionPane.showMessageDialog(app.getAppFrame(),
                ratePane,
                "Screen-shot Frame Rate",
                JOptionPane.QUESTION_MESSAGE);
    }

    protected String getPreferencesKey() {
        return "frame-rate";
    }

    protected void setPreferenceValue(int value) {
        getApplication().getPreferences().putInt(getPreferencesKey(), value);
        getApplication().savePreferences();
    }

    protected int getPreferenceValue() {
        return getApplication().getPreferences().getInt(getPreferencesKey(), FPS_INIT);
    }

    public int getRate() {
        return getPreferenceValue();
    }

    private JPanel createScalePane() {
        ChangeListener action = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider   slider = (JSlider)e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int fps = slider.getValue();
                    if (fps < 1) fps = 1;
                    
                    setPreferenceValue( fps );
                    getApplication().setFrameRate(fps);
                }
            }
        };
        JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, getPreferenceValue());
        framesPerSecond.addChangeListener(action);
        framesPerSecond.setMajorTickSpacing(10);
        framesPerSecond.setMinorTickSpacing(1);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);

        JPanel   p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBorder( BorderFactory.createTitledBorder("Frame Rate") );
        p.add(framesPerSecond);

        return p;
    }

}
