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
    private Logger          log = Logger.getLogger(ScreenshotTimer.class);
    private AtomicBoolean   inProgress = new AtomicBoolean(false);
    private AndroidDevice   device;
    private DeviceFrame     frame;
    private int             errCount = 0;
    private Application app;
    private static final Timer timer;

    static {
        timer = new Timer("Screenshot Timer");
    }

    public ScreenshotTimer(AndroidDevice device, DeviceFrame frame, Application app) {
        this.device = device;
        this.frame = frame;
        this.app = app;
    }
    
    public ScreenshotTimer start(int shotsPerMinute) {
        long updatePeriod = 60 * 1000 / shotsPerMinute;
        timer.scheduleAtFixedRate(this, 0, updatePeriod);
        return this;
    }

    public void stop() {
        this.cancel();
    }

    @Override
    public void run() {
        if (inProgress.getAndSet(true)) return;

        try {
            BufferedImage image = device.getScreenShot(frame.isLandscapeMode());
            frame.setLastScreenshot(image);
        } catch (Exception e) {
            errCount++;
            log.warn(String.format("Failed to get screenshot(%d): %s", errCount, e.getMessage()));

            if (errCount > 5) {
                stop();
                if (e.getMessage().endsWith("device offline")) {
                    app.getAppFrame().getStatusBar().message(device.getName() + " is offline");
                }
                app.hideDevice(frame);
            }
        } finally {
            inProgress.set(false);
        }
    }
    
}
