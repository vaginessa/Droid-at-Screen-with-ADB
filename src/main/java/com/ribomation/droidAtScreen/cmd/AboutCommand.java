package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.gui.GuiUtil;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Shows some info about this app.
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class AboutCommand extends Command {
    public AboutCommand() {
        setLabel("About...");
        setTooltip("Shows info about this application");
        setIcon("about");
        setMnemonic('A');
    }

    @Override
    protected void doExecute(Application app) {
        ImageIcon image = GuiUtil.loadPicture("jens-riboe");
        String aboutText = loadResource("/about.html");
        String linkText = loadResource("/about-links.html");
        String systemText = loadResource("/about-system.html");
        systemText = String.format(systemText, System.getProperty("os.name"),
                System.getProperty("os.arch"),
                System.getProperty("java.vm.name"),
                System.getProperty("java.runtime.version"));

        JPanel content = new JPanel(new BorderLayout(5, 0));
        content.add(new JLabel(image), BorderLayout.WEST);
        content.add(new JLabel("<html>" + aboutText), BorderLayout.CENTER);
        content.add(new JLabel("<html>"+linkText+systemText), BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(null,
                content,
                app.getName() + " - Version " + app.getVersion(),
                JOptionPane.PLAIN_MESSAGE);
    }

    String loadResource(String path) {
        InputStream is = this.getClass().getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException("Failed to load text resource: " + path);
        }

        try {
            StringBuilder   buf = new StringBuilder(1000);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = in.readLine()) != null) {
                buf.append(line);
            }
            in.close();

            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load text resource: " + path, e);
        }
    }
}
