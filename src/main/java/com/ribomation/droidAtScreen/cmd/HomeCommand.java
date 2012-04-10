/*
 * Project:  droidAtScreen
 * File:     HomeCommand.java
 * Modified: 2012-04-11
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

/**
 * Shows the help text.
 * 
 * @user Jens
 * @date 2012-04-11, 00:00
 */
public class HomeCommand extends Command {

    public HomeCommand() {
        setLabel("Home");
        setTooltip("Views the web page for Droid@Screen");
        setIcon("home");
        setMnemonic('O');
    }

    @Override
    protected void doExecute(Application app) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(app.getInfo().getAppUri()));
                    return;
                } catch (Exception e) {
                    getLog().warn("Invalid URI", e);
                }
            }
        }

        JOptionPane.showMessageDialog(app.getAppFrame(), "Browser launch, not supported", "", JOptionPane.WARNING_MESSAGE);
    }
    
}
