/*
 * Project:  droidAtScreen
 * File:     ShowCommand.java
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
import com.ribomation.droidAtScreen.gui.DeviceFrame;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class ShowCommand extends Command  {
    public ShowCommand() {
        setLabel("Show");
        setTooltip("Shows the current device");
        setEnabledOnlyWithDevice(true);
    }

    @Override
    protected void doExecute(Application app) {
        DeviceFrame selectedDevice = app.getSelectedDevice();
        if (selectedDevice == null) return;
        
        selectedDevice.setVisibleEnabled(true);
    }
}
