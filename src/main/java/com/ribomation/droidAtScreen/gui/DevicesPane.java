/*
 * Project:  droidAtScreen
 * File:     DevicesPane.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2011-10-02 15:57
 */
@Deprecated
public class DevicesPane extends JPanel {
    private Map<String, DeviceFrame> devices = new LinkedHashMap<String, DeviceFrame>();

    public DevicesPane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    public void add(DeviceFrame device) {
        devices.put(device.getName(), device);

        DeviceRow row = new DeviceRow(device);

        row.setAlignmentX(LEFT_ALIGNMENT);
        add(row);

        Dimension sz = new Dimension(row.getPreferredSize().width + 10,
                                     devices.size() * (row.getPreferredSize().height + 5));
        this.setMinimumSize(sz);
        this.setPreferredSize(sz);
    }


    public class DeviceRow extends JPanel {
        private final int   MARGIN = 5;
        private final int[] scales = {25,50,75,100,125,150,200,250,300};
        private final int[] rates  = {1,15,30,60,100};

        private DeviceFrame device;
        private JLabel name;
        private JCheckBox   visible;
        private JCheckBox   landscape;
        private JComboBox   scale;
        private JComboBox   rate;
        private JButton     capture;

        public DeviceRow(DeviceFrame device) {
            this.device = device;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            name = new JLabel();
            visible = new JCheckBox("Visible", false);
            landscape = new JCheckBox("Landscape", false);
            scale = new JComboBox(toStringArray("%2d%%", scales));
            rate = new JComboBox(toStringArray("%3d f/m", rates));
            capture = new JButton("Capture");

            name.setText("####################");
            Dimension nameSZ = name.getPreferredSize();
            name.setText(String.format("%-20.18s", device.getName()));
            name.setMinimumSize(nameSZ);
            name.setPreferredSize(nameSZ);

            scale.setMaximumSize(scale.getPreferredSize());
            rate.setMaximumSize(rate.getPreferredSize());
            capture.setMaximumSize(capture.getPreferredSize());

            add(Box.createHorizontalStrut(MARGIN));
            add(name);
            add(Box.createHorizontalStrut(MARGIN));
            add(visible);
            add(Box.createHorizontalStrut(MARGIN));
            add(landscape);
            add(Box.createHorizontalStrut(MARGIN));
            add(scale);
            add(Box.createHorizontalStrut(MARGIN));
            add(rate);
            add(Box.createHorizontalStrut(MARGIN));
            add(capture);
            add(Box.createHorizontalStrut(MARGIN));

        }

        private String[] toStringArray(String format, int[] values) {
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = String.format(format, values[i]);
            }
            return result;
        }
        
        
        
    }

}
