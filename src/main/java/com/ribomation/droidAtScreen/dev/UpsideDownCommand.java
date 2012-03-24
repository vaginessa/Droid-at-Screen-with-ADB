/*
 * Project:  droidAtScreen
 * File:     UpsideDownCommand.java
 * Modified: 2012-03-24
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

/**
 * Flips the device-frame 90 degrees.
 * <p/>
 * User: Jens
 * Created: 2012-03-22, 22:18
 */
public class UpsideDownCommand extends CommandWithTarget<DeviceFrame> {

    public UpsideDownCommand(DeviceFrame deviceFrame) {
        super(deviceFrame);
        setIcon("upsidedown");
        setTooltip("Flips the image upside-down. Useful for ZTE Blade devices.");
        updateButton(deviceFrame);
    }

    @Override
    protected void doExecute(Application app, DeviceFrame deviceFrame) {
        deviceFrame.setUpsideDown(!deviceFrame.isUpsideDown());
        deviceFrame.validate();
    }

    protected void updateButton(DeviceFrame deviceFrame) {
        
    }
    
}
