package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class AutoShowCommand extends CheckBoxCommand {
    public AutoShowCommand() {
        setLabel("Auto Show");
        setTooltip("Show new devices immediately");
    }

    @Override
    protected void notifyApplication(Application app, boolean selected) {
//        app.setAutoShow(selected);
    }

    @Override
    protected boolean getPreferenceValue() {
        return getApplication().getSettings().isAutoShow();
    }

    @Override
    protected void setPreferenceValue(boolean value) {
        getApplication().getSettings().setAutoShow(value);
    }

    
}
