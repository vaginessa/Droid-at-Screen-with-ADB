package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class LookAndFeelCommand extends Command {
    public LookAndFeelCommand() {
        setLabel("Set Look&Feel");
        setTooltip("Let you choose which Look&Feel to use.");
//        setIcon("quit_16x16");
//        setMnemonic('Q');
    }

    @Override
    protected void doExecute(final Application app) {
        final String lafName = (String) JOptionPane.showInputDialog(app.getAppFrame(),
                "Choose a Look&Feel",
                "Look&Feel",
                JOptionPane.QUESTION_MESSAGE,
                null,
                toNames(UIManager.getInstalledLookAndFeels()),
                UIManager.getLookAndFeel().getName());
        if (lafName == null) return;

        Runnable task = new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel( findClassName(lafName) );
                    SwingUtilities.updateComponentTreeUI(app.getAppFrame());
                    app.getAppFrame().pack();
                } catch (Exception e) {}
            }
        };
        SwingUtilities.invokeLater(task);
    }

    protected String[] toNames(UIManager.LookAndFeelInfo[] info) {
        Set<String> names = new TreeSet<String>();
        for (UIManager.LookAndFeelInfo i : info) {
            names.add( i.getName() );
        }
        return names.toArray(new String[names.size()]);
    }

    protected String findClassName(String lafName) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals(lafName)) {
                return info.getClassName();
            }
        }
        return null;
    }

}
