package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.cmd.Command;
import com.ribomation.droidAtScreen.cmd.FrameRateCommand;
import com.ribomation.droidAtScreen.dev.AndroidDevice;
import com.ribomation.droidAtScreen.dev.ScreenImage;
import com.ribomation.droidAtScreen.dev.ScreenshotTimer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Frame holder for the device image.
 *
 * @user jens
 * @date 2010-jan-17 22:13:20
 */
public class DeviceFrame extends JFrame {
    private final RenderingHints    HINTS = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//    private static AtomicInteger    frameCount = new AtomicInteger(1);

    private final Logger              log;
    private Application         app;
    private AndroidDevice       device;

    private int                 scalePercentage = 100;
    private boolean             landscapeMode = false;
    private boolean             upsideDown = false;
    private boolean             visibleEnabled = false;

    private ScreenImage         lastScreenshot;
    private ScreenshotTimer     timer;
    private ImageCanvas         canvas;
    private AffineTransform     scaleTX;
    private AffineTransform     upsideDownTX;

        
    public DeviceFrame(Application app, AndroidDevice device, boolean landscape, boolean upsideDown, int scalePercentage, int frameRate) {
        this.app = app;
        this.device = device;
        this.log = Logger.getLogger(DeviceFrame.class.getName() + ":" + device.getName());
        log.debug(String.format("DeviceFrame(device=%s, landscape=%s, upsideDown=%s, scalePercentage=%d, frameRate=%d)",
                device, landscape, upsideDown, scalePercentage, frameRate));

        setLandscapeMode(landscape);
        setScale(scalePercentage);
        setFrameRate(frameRate);
        setUpsideDown(upsideDown);
        
        setResizable(false);
        setIconImage(GuiUtil.loadIcon("device").getImage());
        canvas = new ImageCanvas();

        setTitle(device.getName());
        add(canvas, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.debug("windowClosing");
                timer.stop();
                timer = null;
                setVisibleEnabled(false);
            }
        });
    }

    public AndroidDevice getDevice() { return device; }

    public String getName() { return device.getName(); }

    public boolean isVisibleEnabled() { return visibleEnabled; }

    public void setVisibleEnabled(boolean visibleEnabled) {
        log.debug("setVisibleEnabled: " + visibleEnabled);
        this.visibleEnabled = visibleEnabled;
        
        if (!isVisibleEnabled()) {
            log.debug("setVisibleEnabled: HIDING");
            super.setVisible(false);
        } else if (!isVisible()) {
            int rate = app.getSettings().getFrameRate();
            setFrameRate(rate);
        }
    }

    @Override
    public void setVisible(boolean show) {
        if (show) return; //we want to delay the frame until we have a proper size
        setVisibleEnabled(false);
    }

    private void updateSize(int width, int height) {
        Insets      margins = getInsets();
        Dimension   frameSize = new Dimension(margins.left + scale(width)  + margins.right,
                margins.top  + scale(height) + margins.bottom);
        Dimension   currentSize = getSize();

        if (!currentSize.equals(frameSize)) {
            log.debug(String.format("updateSize: size=%s", frameSize));
            setSize(frameSize);
        }

        if (!isVisible() && isVisibleEnabled()) {
            setLocationByPlatform(true);
            super.setVisible(true);
        }
    }

    class ImageCanvas extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            if (g instanceof Graphics2D) {
                Graphics2D      g2 = (Graphics2D) g;
                BufferedImageOp tx = null;

                if (scaleTX != null) {
                    tx = new AffineTransformOp(scaleTX, HINTS);
                }

                if (upsideDownTX != null) {
                    if (tx == null) {
                        tx = new AffineTransformOp(upsideDownTX, HINTS);
                    } else {
                        AffineTransform SCTX = (AffineTransform) scaleTX.clone();
                        SCTX.concatenate(upsideDownTX);
                        tx = new AffineTransformOp(SCTX, HINTS);
                    }
                }

                g2.drawImage(lastScreenshot.toBufferedImage(), tx, 0, 0);
            }
        }
    }

    public void setLastScreenshot(ScreenImage image) {
        lastScreenshot = image;
        if (landscapeMode) lastScreenshot.rotate();
        updateSize(lastScreenshot.getWidth(), lastScreenshot.getHeight());
        canvas.repaint();
    }

    public ScreenImage getLastScreenshot() {
        return lastScreenshot;
    }

    public void setFrameRate(int frameRate) {
        if (timer != null) timer.stop();
        timer = new ScreenshotTimer(device, this, app).start(frameRate);
    }

    public void setLandscapeMode(boolean landscape) {
        this.landscapeMode = landscape;
    }

    public void setScale(int scalePercentage) {
        this.scalePercentage = scalePercentage;
        if (scalePercentage == 100) {
            scaleTX = null;
        } else {
            double scale = scalePercentage / 100.0;
            scaleTX = AffineTransform.getScaleInstance(scale, scale);
        }
    }

    public void setUpsideDown(boolean upsideDown) {
        this.upsideDown = upsideDown;
        if (upsideDown && lastScreenshot != null) {
            double x = lastScreenshot.getWidth() / 2;
            double y = lastScreenshot.getHeight() / 2;
            upsideDownTX = AffineTransform.getQuadrantRotateInstance(2, x, y);
        } else {
            upsideDownTX = null;
        }
    }

    private int scale(int value) {
        if (scalePercentage == 100) return value;
        return (int) Math.round(value * scalePercentage / 100.0);
    }



    
}
