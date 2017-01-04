/*
 * Project:  droidAtScreen
 * File:     UpsideDownCommand.java
 * Modified: 2012-03-24
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.gui.DeviceFrame;

/**
 * Selects whether to capture RAW or PNG from device.
 */
public class CaptureModeCommand extends CommandWithTarget<DeviceFrame> {

	public CaptureModeCommand(DeviceFrame deviceFrame) {
		super(deviceFrame);
		updateButton(deviceFrame);
	}

	@Override
	protected void doExecute(Application app, DeviceFrame deviceFrame) {
		deviceFrame.setRawImage(!deviceFrame.isRawImage());
		updateButton(deviceFrame);
		deviceFrame.repaint();
	}

	@Override
	protected void updateButton(DeviceFrame deviceFrame) {
		setIcon(deviceFrame.isRawImage() ? "raw" : "png");
		setTooltip(String.format("Device screenshot capture format (%s).", deviceFrame.isRawImage() ? "RAW" : "PNG"));
	}
}
