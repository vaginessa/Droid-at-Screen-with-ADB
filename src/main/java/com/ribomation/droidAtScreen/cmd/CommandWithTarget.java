/*
 * Project:  droidAtScreen
 * File:     CommandWithTarget.java
 * Modified: 2012-03-22
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

/**
 * A command that is associated with a target object, which is the subject for the execution of the command.
 * <p/>
 * User: Jens
 * Created: 2012-03-22, 22:13
 */
public abstract class CommandWithTarget<TargetType> extends Command {
    private TargetType      target;
    
    protected CommandWithTarget(TargetType target) {
        this.target = target;
    }

    public TargetType getTarget() {
        return target;
    }

    @Override
    public AbstractButton newButton() {
        JToggleButton b = new JToggleButton(this);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setRolloverEnabled(true);
        return b;
    }

    protected abstract void updateButton(TargetType target);

    protected abstract void doExecute(Application app, TargetType target);

    @Override
    final protected void doExecute(Application app) {
        doExecute(app, getTarget());
    }
    
}
