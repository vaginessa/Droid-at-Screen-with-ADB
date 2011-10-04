/*
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

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
