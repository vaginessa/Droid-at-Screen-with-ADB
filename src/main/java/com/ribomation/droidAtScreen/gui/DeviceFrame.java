/*
 * Project:  droidAtScreen
 * File:     DeviceFrame.java
 * Modified: 2011-10-04
 *
 * Copyright (C) 2011, Ribomation AB (Jens Riboe).
 * http://blog.ribomation.com/
 *
 * You are free to use this software and the source code as you like.
 * We do appreciate if you attribute were it came from.
 */

package com.ribomation.droidAtScreen.gui;

import com.ribomation.droidAtScreen.Application;
import com.ribomation.droidAtScreen.dev.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImageOp;
import java.util.*;
import java.util.Timer;

/**
 * Frame holder for the device image.
 *
 * @user jens
 * @date 2010-jan-17 22:13:20
 */
public class DeviceFrame extends JFrame {
    private final static RenderingHints HINTS = new RenderingHints(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

    private final Logger log;
    private final Application app;
    private final AndroidDevice device;

    private int scalePercentage = 100;
    private boolean landscapeMode = false;
    private boolean upsideDown = false;

    private ImageCanvas canvas;
    private JComponent toolBar;
    private AffineTransform scaleTX;
    private AffineTransform upsideDownTX;
    private RecordingListener recordingListener;
    private Timer timer;
    private TimerTask retriever;
    private InfoPane infoPane;

    public DeviceFrame(Application app, AndroidDevice device) {
        this.app = app;
        this.device = device;
        this.log = Logger.getLogger(DeviceFrame.class.getName() + ":" + device.getName());
        log.debug(String.format("DeviceFrame(device=%s)", device));

        setTitle(device.getName());
        setIconImage(GuiUtil.loadIcon("device").getImage());
        setResizable(true);

        add(canvas = new ImageCanvas(), BorderLayout.CENTER);
        add(toolBar = createToolBar(), BorderLayout.WEST);
        add(infoPane = new InfoPane(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.debug("windowClosing");
                retriever.cancel();
                timer.cancel();
                DeviceFrame.this.setVisible(false);
            }
        });

        setLandscapeMode(landscapeMode);
        setScale(scalePercentage);
        setUpsideDown(upsideDown);

        retriever = new Retriever();
        timer = new Timer("Screenshot Timer");
        timer.schedule(retriever, 0, 100);
        pack();
    }

    protected JComponent createToolBar() {
        JPanel buttons = new JPanel(new GridLayout(5, 1, 0, 8));
        buttons.add(new OrientationCommand(this).newButton());
        buttons.add(new ScaleCommand(this).newButton());
        buttons.add(new UpsideDownCommand(this).newButton());
        buttons.add(new ScreenshotCommand(this).newButton());
        buttons.add(new RecordingCommand(this).newButton());

        JPanel tb = new JPanel(new FlowLayout());
        tb.setBorder(BorderFactory.createEmptyBorder());
        tb.add(buttons);

        return tb;
    }
    
    public class InfoPane extends JPanel {
        JLabel  size;
        
        InfoPane() {
            super(new FlowLayout(FlowLayout.CENTER));
            size = new JLabel("No screenshot");
            this.add(size);
            setBorder(BorderFactory.createEmptyBorder());
        }

        void setSizeInfo(ImageCanvas img) {
            Dimension sz = img.getPreferredSize();
            size.setText(String.format("%d x %d", sz.width, sz.height));
        }

        public void setMessage(String msg) {
            
        }
    }

    class Retriever extends TimerTask {
        @Override
        public void run() {
            ScreenImage image = device.getScreenImage();
            if (image != null) {
                if (landscapeMode) image.rotate();
                if (recordingListener != null) recordingListener.record(image);
                canvas.setScreenshot(image);
                infoPane.setSizeInfo(canvas);
            }
            pack();
        }
    }

    class ImageCanvas extends JComponent {
        private ScreenImage image;

        public ImageCanvas() {
            setBorder(BorderFactory.createLoweredBevelBorder());
        }

        public void setScreenshot(ScreenImage image) {
            this.image = image;
        }

        public ScreenImage getScreenshot() {
            return image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (image != null && g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;
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

                g2.drawImage(image.toBufferedImage(), tx, 0, 0);
            } else {
                g.setColor(Color.RED);
                g.setFont(getFont().deriveFont(16.0F));
                g.drawString("No screenshot yet", 10, 25);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            if (image == null) return new Dimension(200, 300);
            return new Dimension(scale(image.getWidth()), scale(image.getHeight()));
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
    }

    public void setLandscapeMode(boolean landscape) {
        this.landscapeMode = landscape;
        pack();
    }

    public void setScale(int scalePercentage) {
        this.scalePercentage = scalePercentage;
        if (scalePercentage == 100) {
            scaleTX = null;
        } else {
            double scale = scalePercentage / 100.0;
            scaleTX = AffineTransform.getScaleInstance(scale, scale);
        }
        pack();
    }

    public void setUpsideDown(boolean upsideDown) {
        this.upsideDown = upsideDown;
        ScreenImage lastScreenshot = getLastScreenshot();
        if (upsideDown && lastScreenshot != null) {
            double x = lastScreenshot.getWidth() / 2;
            double y = lastScreenshot.getHeight() / 2;
            upsideDownTX = AffineTransform.getQuadrantRotateInstance(2, x, y);
        } else {
            upsideDownTX = null;
        }
        pack();
    }

    public void setRecordingListener(RecordingListener recordingListener) {
        this.recordingListener = recordingListener;
    }

    public ScreenImage getLastScreenshot() {
        return canvas.getScreenshot();
    }

    public InfoPane getInfoPane() {
        return infoPane;
    }

    public AndroidDevice getDevice() {
        return device;
    }

    public String getName() {
        return device.getName();
    }

    public boolean isLandscapeMode() {
        return landscapeMode;
    }

    public int getScale() {
        return scalePercentage;
    }

    public boolean isUpsideDown() {
        return upsideDown;
    }

    private int scale(int value) {
        if (scalePercentage == 100) return value;
        return (int) Math.round(value * scalePercentage / 100.0);
    }

}
