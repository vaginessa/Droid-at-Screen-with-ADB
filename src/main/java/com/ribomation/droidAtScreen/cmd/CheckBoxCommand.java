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
 * @date 2010-jan-19 10:36:37
 */
public abstract class CheckBoxCommand extends Command {
    @Override
    public JMenuItem newMenuItem() {
        JCheckBoxMenuItem b = new JCheckBoxMenuItem();
        b.setSelected(getPreferenceValue());
        return b;
    }

    @Override
    public AbstractButton newButton() {
        AbstractButton b = new JCheckBox();
        b.setSelected(getPreferenceValue());
        return b;
    }

    @Override
    protected final void doExecute(Application app) {
        boolean selected = !getPreferenceValue();
        setPreferenceValue( selected );
        notifyApplication(app, selected);
    }

    public boolean  isSelected() { return getPreferenceValue(); }

    protected abstract void notifyApplication(Application app, boolean selected);

    protected abstract boolean getPreferenceValue();

    protected abstract void setPreferenceValue(boolean value);

}
