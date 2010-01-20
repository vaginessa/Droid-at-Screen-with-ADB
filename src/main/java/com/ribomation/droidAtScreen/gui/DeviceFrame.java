package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.dev.AndroidDevice;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DESCRIPTION
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
//        super("Android Device - " + device.toNames());
        log.debug(String.format("DeviceFrame(device=%s, portrait=%s, scalePercentage=%d, frameRate=%d)",
                device, portrait, scalePercentage, frameRate));
        this.app = app;
        setFrameName( device.getName() );

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pane.stop();
            }
        });

//        ImageTransformer orientation = new OrientationImageTransformer(portrait);
//        ImageTransformer scale       = new ScaleImageTransformer(scalePercentage);
//        device = new TransformingAndroidDevice(scale, new TransformingAndroidDevice(orientation, device));

        pane   = new DevicePane(this, device, portrait, scalePercentage, frameRate);
//        pane.setPortrait(portrait);
//        pane.setScalePercentage(scalePercentage);
//        pane.setFrameRate(frameRate);

//        JScrollPane scrollPane = new JScrollPane(pane);
//        scrollPane.setSize(pane.getPreferredSize());
        this.add( pane );
        this.pack();
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
