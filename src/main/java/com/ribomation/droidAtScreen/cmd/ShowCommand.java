package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.dev.AndroidDevice;

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
        AndroidDevice selectedDevice = app.getSelectedDevice();
        if (selectedDevice == null) return;
        
        app.showDevice(selectedDevice);
    }
}
