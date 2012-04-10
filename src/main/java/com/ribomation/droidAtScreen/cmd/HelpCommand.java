/*
 * Project:  droidAtScreen
 * File:     HelpCommand.java
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
public class HelpCommand extends Command {

    public HelpCommand() {
        setLabel("Help");
        setTooltip("Views the help text for Droid@Screen");
        setIcon("help");
        setMnemonic('H');
    }

    @Override
    protected void doExecute(Application app) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(app.getInfo().getHelpUri()));
                    return;
                } catch (Exception e) {
                    getLog().warn("Invalid URI", e);
                }
            }
        }

        JOptionPane.showMessageDialog(app.getAppFrame(), "Browser launch, not supported", "", JOptionPane.WARNING_MESSAGE);
    }
    
}
