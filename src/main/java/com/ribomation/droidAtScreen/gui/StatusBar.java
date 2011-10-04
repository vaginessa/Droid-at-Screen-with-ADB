package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;
import java.awt.*;

/**
 * Place for status messages.
 *
 * @user jens
 * @date 2011-10-02 11:17
 */
public class StatusBar extends JPanel {
    private JLabel      message;

    public StatusBar(Application app) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createLoweredBevelBorder());

        message = new JLabel("", JLabel.LEFT);
        Font font = message.getFont();
        message.setFont(font.deriveFont(Font.PLAIN, (float) (font.getSize() * 0.90)));
        message.setForeground(Color.DARK_GRAY);
        add(message);
        
        message(app.getInfo().getName() + ", V" + app.getInfo().getVersion());
    }

    public void message(String txt) {
        message.setText(txt);
    }

    public void clear() {
        message("");
    }

}
