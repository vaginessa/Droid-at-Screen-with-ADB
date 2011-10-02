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
