/*
 * Project:  droidAtScreen
 * File:     MailCommand.java
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
public class MailCommand extends Command {

    public MailCommand() {
        setLabel("Mail");
        setTooltip("Send a feedback mail to the developer");
        setIcon("mail");
        setMnemonic('M');
    }

    @Override
    protected void doExecute(Application app) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.MAIL)) {
                try {
                    desktop.browse(new URI(app.getInfo().getMailUri()));
                    return;
                } catch (Exception e) {
                    getLog().warn("Invalid URI", e);
                }
            }
        }

        JOptionPane.showMessageDialog(app.getAppFrame(), "Mail client launch, not supported", "", JOptionPane.WARNING_MESSAGE);
    }
    
}
