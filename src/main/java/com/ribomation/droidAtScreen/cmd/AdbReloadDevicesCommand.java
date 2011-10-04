/*
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;

/**
 * Reloads the devices from ADB.
 *
 * @user jens
 * @date 2011-10-04 13:00
 */
public class AdbReloadDevicesCommand extends Command {

    public AdbReloadDevicesCommand() {
        setLabel("Reload Devices");
        setIcon("diagram");
        setTooltip("Reloads all devices from ADB.");
    }

    @Override
    protected void doExecute(final Application app) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.getDeviceManager().reloadDevices();
                app.getAppFrame().getStatusBar().message("Android devices reloaded");
            }
        });
    }

}
