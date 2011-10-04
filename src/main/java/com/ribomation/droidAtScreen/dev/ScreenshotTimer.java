/*
 * Project:  droidAtScreen
 * File:     ScreenshotTimer.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.dev;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.gui.DeviceFrame;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Timer that take screenshots from a device.
 *
 * @user jens
 * @date 2011-10-01 14:55
 */
public class ScreenshotTimer extends TimerTask {
    private static final int MAX_ERRORS = 5;
    private Logger          log = Logger.getLogger(ScreenshotTimer.class);
    private AtomicBoolean   inProgress = new AtomicBoolean(false);
    private AndroidDevice   device;
    private DeviceFrame     frame;
    private int             errCount = 0;
    private Application     app;
    private Timer           timer;

    public ScreenshotTimer(AndroidDevice device, DeviceFrame frame, Application app) {
        this.device = device;
        this.frame = frame;
        this.app = app;
    }
    
    public ScreenshotTimer start(int shotsPerMinute) {
        long updatePeriod = 60 * 1000 / shotsPerMinute;
        timer = new Timer("Screenshot Timer");
        timer.scheduleAtFixedRate(this, 0, updatePeriod);
        return this;
    }

    public void stop() {
        this.cancel();
        timer = null;
    }

    @Override
    public void run() {
        if (inProgress.getAndSet(true)) return;

        try {
            ScreenImage image = device.getScreenImage();
            frame.setLastScreenshot(image);
        } catch (Exception e) {
            errCount++;
            log.warn(String.format("Failed to get screenshot(%d): %s", errCount, e.getMessage()));

            if (errCount > MAX_ERRORS) {
                stop();

                String msg = e.getMessage();
                if (e.getCause() != null) {
                    msg = e.getCause().getMessage();
                }
                if (msg == null || msg.trim().length() == 0) {
                    msg = e.toString();
                }
                if (msg.endsWith("device offline")) {
                    app.getAppFrame().getStatusBar().message(device.getName() + " is offline");
                }
                
                frame.setVisibleEnabled(false);
                frame.dispose();
            }
        } finally {
            inProgress.set(false);
        }
    }
    
}
