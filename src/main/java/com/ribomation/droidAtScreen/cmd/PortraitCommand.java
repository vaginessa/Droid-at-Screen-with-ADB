package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
@Deprecated
public class PortraitCommand extends CheckBoxCommand {

    public PortraitCommand() {
        setLabel("Portrait");
        setTooltip("Show the device in portrait mode");
    }

    @Override
    protected void notifyApplication(Application app, boolean selected) {
        app.setPortraitMode(selected);
    }

    @Override
    protected String getPreferencesKey() {
        return "portrait-mode";
    }

}
