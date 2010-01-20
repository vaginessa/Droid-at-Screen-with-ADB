package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class QuitCommand extends Command {
    public QuitCommand() {
        setLabel("Quit");
        setTooltip("Quits the application");
        setIcon("quit_16x16");
        setMnemonic('Q');
    }

    @Override
    protected void doExecute(Application app) {
        int rc = JOptionPane.showConfirmDialog(app.getAppFrame(),
                "Do you really want to quit?",
                "Quit?",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (rc == JOptionPane.OK_OPTION) {
            JFrame f = app.getAppFrame();
            if (f != null) {
                f.dispose();
            }
            System.exit(0);
        }
    }
}
