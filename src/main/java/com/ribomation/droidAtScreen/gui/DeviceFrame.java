package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.dev.AndroidDevice;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Frame holder for the device image.
 *
 * @user jens
 * @date 2010-jan-17 22:13:20
 */
public class DeviceFrame extends JFrame {
    private Logger          log = Logger.getLogger(DeviceFrame.class);
    private Application     app;
    private DevicePane      pane;
    private static AtomicInteger    frameCount = new AtomicInteger(1);

    public DeviceFrame(Application app, AndroidDevice device, boolean portrait, int scalePercentage, int frameRate) {
        this(app, device, portrait, false, scalePercentage, frameRate);
    }
    public DeviceFrame(Application app, AndroidDevice device, boolean portrait, boolean upsideDown, int scalePercentage, int frameRate) {
        log.debug(String.format("DeviceFrame(device=%s, portrait=%s, upsideDown=%s, scalePercentage=%d, frameRate=%d)",
                device, portrait, upsideDown, scalePercentage, frameRate));
        this.app = app;
        setFrameName( device.getName() );

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pane.stop();
            }
        });

        pane   = new DevicePane(this, device, portrait, upsideDown, scalePercentage, frameRate);
        this.add( pane );
        this.pack();
    }

    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
//        super.paintComponents(g);
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            log.debug("is Graphics2D");
        }
        final Dimension size = this.getSize();
        final Insets insets = this.getInsets();
        final BufferedImage img = pane.getLastScreenShot();
        log.debug(String.format("size=%s, margin=%s", size, insets));
        log.debug(String.format("img.size=%s", new Dimension(img.getWidth(), img.getHeight())));
    }

    protected void setFrameName(String devName) {
        int cnt = frameCount.getAndIncrement();
        setTitle(devName + (cnt > 1 ? ":"+cnt : ""));
    }

    public String getFrameName() {
        return getTitle();
    }

    public AndroidDevice getDevice() {
        return pane.getDevice();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        pane.start();
    }

    @Override
    public void dispose() {
        log.debug("disposed");
        pane.stop();
        app.hideDevice(this, false);
        
        pane = null;
        super.dispose();
    }
}
