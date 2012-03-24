/*
 * Project:  droidAtScreen
 * File:     SkipEmulatorCommand.java
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

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 10:35:20
 */
public class SkipEmulatorCommand extends CheckBoxCommand {

    public SkipEmulatorCommand() {
        setLabel("Skip Emulators");
        setTooltip("Do not show emulators");
    }

    @Override
    protected void notifyApplication(Application app, boolean selected) {
//        app.setSkipEmulator(selected);
    }

//    @Override
//    protected String getPreferencesKey() {
//        return "skip-emulator";
//    }

    @Override
    protected boolean getPreferenceValue() {
        return getApplication().getSettings().isHideEmulators();
    }

    @Override
    protected void setPreferenceValue(boolean value) {
        getApplication().getSettings().setHideEmulators(value);
    }
}
