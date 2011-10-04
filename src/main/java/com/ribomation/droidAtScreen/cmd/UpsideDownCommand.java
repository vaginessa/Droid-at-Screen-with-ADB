/*
 * Project:  droidAtScreen
 * File:     UpsideDownCommand.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

/**
 * Applies one or more transformations of the device image.
 *
 * @user jens
 * @date 2011-09-30 15:19
 */
public class UpsideDownCommand extends CheckBoxCommand  {

    public UpsideDownCommand() {
        setLabel("Upside-Down");
        setIcon("upsidedown");
        setTooltip("Flips the image upside-down. Useful for ZTE Blade devices.");
        setEnabledOnlyWithDevice(true);
    }

    @Override
    protected void notifyApplication(Application app, boolean upsideDown) {
        app.setUpsideDown(upsideDown);
    }

    @Override
    protected boolean getPreferenceValue() {
        return getApplication().getSettings().isUpsideDown();
    }

    @Override
    protected void setPreferenceValue(boolean value) {
        getApplication().getSettings().setUpsideDown(value);
    }
}
