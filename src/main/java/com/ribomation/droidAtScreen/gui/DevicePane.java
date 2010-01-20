package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.dev.AndroidDevice;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Renders the screen shots of a connected Android device.
 *
 * @user jens
 * @date 2010-jan-17 20:43:00
 */
public class DevicePane extends JPanel {
    private final RenderingHints    hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    private final Logger            log = Logger.getLogger(DevicePane.class);
    private BufferedImage       lastScreenShot;
    private DeviceFrame deviceFrame;
    private AndroidDevice       device;
    private Timer               timer;
    private int                 frameRate = 15, scalePercentage = 100;
    private boolean             portrait = true;
    private AffineTransformOp   transformOP = null;

    public DevicePane(DeviceFrame deviceFrame, AndroidDevice dev, boolean portrait, int scalePercentage, int frameRate) {
        super(new BorderLayout(), true);
        this.deviceFrame = deviceFrame;
        this.device = dev;
        setPortrait(portrait);
        setScalePercentage(scalePercentage);
        setFrameRate(frameRate);
    }

    public AndroidDevice getDevice() {
        return device;
    }

    public void setPortrait(boolean portrait) {
        this.portrait = portrait;
        updateSize();
    }

    public void setScalePercentage(int scalePercentage) {
        if ((scalePercentage < 10) || (300 < scalePercentage)) {
            throw new IllegalArgumentException("Scale % is invalid: " + scalePercentage);
        }
        this.scalePercentage = scalePercentage;

        if (scalePercentage != 100) {
            double scale = scalePercentage / 100.0;
            AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
            transformOP = new AffineTransformOp(tx, hints);
        } else {
            transformOP = null;
        }
        updateSize();
    }

    public void setFrameRate(int rate) {
        if (rate <= 0) {
            throw new IllegalArgumentException("Frame rate must be a positive number. Value=" + rate);
        }
        this.frameRate = rate;
    }

    public BufferedImage getLastScreenShot() {
        return lastScreenShot;
    }

    public void setLastScreenShot(BufferedImage lastScreenShot) {
        this.lastScreenShot = lastScreenShot;
    }

    protected void paintComponent(Graphics g) {
        BufferedImage img = getLastScreenShot();
        if (img == null) {
            return;
        }

        int x = (this.getWidth() - img.getWidth()) / 2;
        int y = (this.getHeight() - img.getHeight()) / 2;
        g.drawImage(img, x, y, this);
    }

    private BufferedImage fetchScreenshot() {
        BufferedImage img = device.getScreenShot(!this.portrait);
        if (transformOP != null && img != null) {
            img = transformOP.filter(img, null);
        }
        return img;
    }

    protected void  updateSize() {
        updateSize( fetchScreenshot() );
    }

    protected void  updateSize(BufferedImage img) {
        Dimension sz = new Dimension(img.getWidth(), img.getHeight());
        this.setMinimumSize(sz);
        this.setSize(sz);
        
        deviceFrame.setPreferredSize( add(sz, deviceFrame.getInsets()) );
        deviceFrame.pack();
    }

    protected Dimension add(Dimension sz, Insets pad) {
        return new Dimension(pad.left + sz.width + pad.right, pad.top + sz.height + pad.bottom);
    }

    public void     update() {
        setLastScreenShot( fetchScreenshot() );
        repaint();
    }

    public void start() {
        long updatePeriod = 1000L / frameRate;
        timer = new Timer("ScreenShot Updater");
        timer.scheduleAtFixedRate(new Updater(), 2*updatePeriod, updatePeriod);
    }

    public void  stop() {
        timer.cancel();
        timer = null;
    }

    class Updater extends TimerTask {
        private AtomicBoolean   busy = new AtomicBoolean(false);

        @Override
        public void run() {
            if (busy.getAndSet(true)) return;
            try {
                update();
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
            busy.set(false);
        }
    }

}
