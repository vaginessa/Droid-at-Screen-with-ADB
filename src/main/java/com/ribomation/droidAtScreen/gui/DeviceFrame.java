package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;
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
    private static AtomicInteger    frameCount = new AtomicInteger(1);

    private Logger              log = Logger.getLogger(DeviceFrame.class);
    private Application         app;
    private AndroidDevice       device;

    private int                 scalePercentage = 100;
    private boolean             landscapeMode = false;
    private boolean             upsideDown = false;

    private ScreenImage         lastScreenshot;
    private ScreenshotTimer     timer;
    private ImageCanvas         canvas;
    private AffineTransform     scaleTX;
    private AffineTransform     upsideDownTX;

        
    public DeviceFrame(Application app, AndroidDevice device, boolean portrait, boolean upsideDown, int scalePercentage, int frameRate) {
        log.debug(String.format("DeviceFrame(device=%s, portrait=%s, upsideDown=%s, scalePercentage=%d, frameRate=%d)",
                device, portrait, upsideDown, scalePercentage, frameRate));

        this.app = app;
        this.device = device;

        setLandscapeMode(!portrait);
        setScale(scalePercentage);
        setFrameRate(frameRate);
        setUpsideDown(upsideDown);
        
        setResizable(false);
        setIconImage(GuiUtil.loadIcon("device").getImage());
        canvas = new ImageCanvas();

        setFrameName( device.getName() );
        add(canvas, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { timer.stop(); }
        });
    }

    @Override
    public void setVisible(boolean show) {
        if (show) return; //we want to delay the frame until we have a proper size
        super.setVisible(false);
    }

    public void setLandscapeMode(boolean landscape) {
        this.landscapeMode = landscape;
    }

    public void setFrameRate(int frameRate) {
        if (timer != null) timer.cancel();
        timer = new ScreenshotTimer(device, this, app).start(frameRate);
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

    public void setLastScreenshot(ScreenImage image) {
        lastScreenshot = image;
        updateSize(lastScreenshot.getWidth(), lastScreenshot.getHeight());
        canvas.repaint();
    }

    private void updateSize(int width, int height) {
        if (landscapeMode) {
            int oldWidth = width;
            width  = height;
            height = oldWidth;
        }

        Insets      margins = this.getInsets();
        Dimension   frameSize = new Dimension(margins.left + scale(width)  + margins.right,
                                              margins.top  + scale(height) + margins.bottom);
        Dimension   currentSize = this.getSize();
        if (currentSize.equals(frameSize)) return;

        log.debug(String.format("updateSize: size=%s", frameSize));
        this.setMinimumSize(frameSize);
        this.setMaximumSize(frameSize);
        this.setSize(frameSize);

        if (!isVisible()) {
//            GuiUtil.placeInCenterScreen(this);
            this.setLocationByPlatform(true);
            super.setVisible(true);
        }
    }

    private int scale(int value) {
        if (scalePercentage == 100) return value;
        return (int) Math.round(value * scalePercentage / 100.0);
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

                if (landscapeMode) lastScreenshot.rotate();
                g2.drawImage(lastScreenshot.toBufferedImage(), tx, 0, 0);
            }
        }
    }


    public AndroidDevice getDevice() {
        return device;
    }

    public String getName() {
        return device.getName();
    }

    protected void setFrameName(String devName) {
        int cnt = frameCount.getAndIncrement();
        setTitle(devName + (cnt > 1 ? ":" + cnt : ""));
    }

    public String getFrameName() {
        return getTitle();
    }
    
}
