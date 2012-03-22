/*
 * Project:  droidAtScreen
 * File:     OrientationCommand.java
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

/**
 * Flips the device-frame 90 degrees.
 * <p/>
 * User: Jens
 * Created: 2012-03-22, 22:18
 */
public class OrientationCommand extends CommandWithTarget<DeviceFrame> {

    public OrientationCommand(DeviceFrame deviceFrame) {
        super(deviceFrame);
        updateLabelAndIcon(deviceFrame);
        setTooltip("Flip the orientation (portrait | landscape)");
    }

    @Override
    protected void doExecute(Application app) {
        DeviceFrame deviceFrame = getTarget();
        deviceFrame.setLandscapeMode(!deviceFrame.isLandscapeMode());
        updateLabelAndIcon(deviceFrame);
        deviceFrame.validate();
    }

    private void updateLabelAndIcon(DeviceFrame deviceFrame) {
        setLabel(deviceFrame.isLandscapeMode() ? "Landscape" : "Portrait ");
        setIcon("orientation-" + getLabel().toLowerCase().trim());
    }

    @Override
    public AbstractButton newButton() {
        JToggleButton b = new JToggleButton(this);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setSelected(true);
        return b;
    }
}
