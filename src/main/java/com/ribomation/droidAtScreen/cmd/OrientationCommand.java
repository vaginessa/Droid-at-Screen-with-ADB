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
 * Flips the device frame orientation.
 *
 * @user jens
 * @date 30 september 2011, 14:19
 */
public class OrientationCommand extends CheckBoxCommand  {

    public OrientationCommand() {
        updateView(getPreferenceValue());
        setTooltip("Flip the orientation (portrait | landscape)");
        setEnabledOnlyWithDevice(true);
    }

    @Override
    protected void notifyApplication(Application app, boolean landscape) {
        app.setLandscapeMode(landscape);
        updateView(landscape);
        app.getAppFrame().validate();
    }

    @Override
    protected boolean getPreferenceValue() {
        return getApplication().getSettings().isLandscape();
    }

    @Override
    protected void setPreferenceValue(boolean value) {
        getApplication().getSettings().setLandscape(value);
    }

    private void updateView(boolean landscape) {
        setLabel(landscape ? "Landscape" : "Portrait ");
        setIcon("orientation-" + getLabel().toLowerCase().trim());
    }

    @Override
    public AbstractButton newButton() {
        JToggleButton b = new JToggleButton(this);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setSelected(getPreferenceValue());
        return b;
    }
}
