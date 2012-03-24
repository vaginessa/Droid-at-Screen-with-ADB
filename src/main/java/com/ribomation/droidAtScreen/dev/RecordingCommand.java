/*
 * Project:  droidAtScreen
 * File:     RecordingCommand.java
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
 * ....
 * <p/>
 * User: Jens
 * Created: 2012-03-24, 15:50
 */
public class RecordingCommand extends CommandWithTarget<DeviceFrame> {

    public RecordingCommand(DeviceFrame deviceFrame) {
        super(deviceFrame);
        
    }

    @Override
    protected void doExecute(Application app, DeviceFrame deviceFrame) {
        
    }

    protected void updateButton(DeviceFrame deviceFrame) {
        
    }
    
}
