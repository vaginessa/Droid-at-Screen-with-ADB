package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;
import java.awt.*;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class AboutCommand extends Command {
    public AboutCommand() {
        setLabel("About...");
        setTooltip("Shows info about this application");
        setIcon("about_16x16");
        setMnemonic('A');
    }

    @Override
    protected void doExecute(Application app) {
        String txt = "<html>This application was a quick hack by<br/>" +
                "Jens Riboe <br/><a href='mailto:jens.riboe@ribomation.com'>jens.riboe@ribomation.com</a><br/>" +
                "<a href='http://blog.ribomation.com/'>http://blog.ribomation.com/</a><br/>" +
                "<i>Stockholm in January 2009</i>.";
        JPanel content = new JPanel(new BorderLayout(5, 0));
        content.add(new JLabel(loadPicture("jens-riboe")), BorderLayout.WEST);
        content.add(new JLabel(txt), BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null,
                content,
                app.getName() + " - Version " + app.getVersion(),
                JOptionPane.PLAIN_MESSAGE);
    }
}
