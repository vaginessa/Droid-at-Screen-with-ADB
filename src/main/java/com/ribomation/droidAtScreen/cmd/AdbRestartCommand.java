/*
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.gui.StatusBar;

import javax.swing.*;

/**
 * Restarts the ADB server.
 *
 * @user jens
 * @date 2011-10-04 13:00
 */
public class AdbRestartCommand extends Command {

    public AdbRestartCommand() {
        setLabel("Restart ADB");
        setIcon("sync");
        setTooltip("Tries to restart the ADB server. Unplug your device(s) first.");
    }

    @Override
    protected void doExecute(final Application app) {
        final StatusBar statusBar = app.getAppFrame().getStatusBar();
        statusBar.message("Restarting ADB...");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boolean succeeded = app.getDeviceManager().restartADB();
                statusBar.message("ADB restart " + (succeeded ? "succeeded" : "failed"));
            }
        });
    }

}
